package bughunter.bughunterserver.service.impl;

import bughunter.bughunterserver.DTO.EdgeDTO;
import bughunter.bughunterserver.DTO.GraphDTO;
import bughunter.bughunterserver.DTO.NodeDTO;
import bughunter.bughunterserver.DTO.wrapper.EdgeDTOWrapper;
import bughunter.bughunterserver.DTO.wrapper.NodeDTOWrapper;
import bughunter.bughunterserver.dao.EdgeDao;
import bughunter.bughunterserver.dao.NodeDao;
import bughunter.bughunterserver.model.entity.Edge;
import bughunter.bughunterserver.model.entity.Node;
import bughunter.bughunterserver.service.EdgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sean
 * @date 2019-01-23.
 */
@Service
public class EdgeServiceImpl implements EdgeService {

    public static int CROWD_WORKER_NUMBER = 100;

    @Autowired
    private EdgeDao edgeDao;

    @Autowired
    private NodeDao nodeDao;

    @Autowired
    private NodeDTOWrapper nodeDTOWrapper;

    @Autowired
    private EdgeDTOWrapper edgeDTOWrapper;

    @Override
    public Edge save(Edge edge) {
        return edgeDao.save(edge);
    }


    @Override
    public Edge getNextBugHint(String currentWindow, String nextWindow) {
        //按照number降序
        List<Edge> edgeList =
                edgeDao.findBySourceNodeAndTargetNodeOrderByNumber(currentWindow, nextWindow);
        //存在isCovered为1的Edge
        if (edgeList.stream().
                anyMatch(edge -> edge.getIsCovered() == 1)) {
            //从isCovered中选
            List<Edge> coveredEdges = edgeList.stream().
                    filter(edge -> edge.getIsCovered() == 1).collect(Collectors.toList());
            return coveredEdges.get(0);
        } else {
            //初始情况,所有number都为0
            if (edgeList.stream().allMatch(edge -> edge.getNumber() == 0)) {
                //随机选择
                return edgeList.get((int) (0 + Math.random() * (edgeList.size() - 0 + 0)));
            } else {
                //按number降序中,选择小于60%人数且最大的
                edgeList.stream().filter(edge -> edge.getNumber() <= 0.6 * CROWD_WORKER_NUMBER);
                return edgeList.get(0);
            }
        }
    }

    @Override
    public List<Edge> getRecommBugs(String appKey, String currentWindow) {
        //当前用户所在节点
        NodeDTO currNodeDTO = nodeDTOWrapper.wrap(nodeDao.findByWindow(currentWindow));
        HashMap<NodeDTO, List<EdgeDTO>> map = new HashMap<>();
        //待测App所有覆盖边的目标节点
        List<Node> nodeList = nodeDao.findByAppKey(appKey);

        List<Edge> edgeList = new ArrayList<>();
        for (Node node : nodeList) {
            List<Edge> handledEdges = new ArrayList<>();
            //节点对应的有向边
            List<Edge> edges = edgeDao.findBySourceNode(node.getWindow());
            //两个节点一个方向下,只能存在一条有向边
            for (Edge edge : edges) {
                if (handledEdges.stream().noneMatch(edge1 -> edge1.getSourceNode().equals(edge.getSourceNode())
                        && edge1.getTargetNode().equals(edge.getTargetNode()))) {
                    //去环
                    if (!edge.getSourceNode().equals(edge.getTargetNode())) {
                        edgeList.add(edge);
                        handledEdges.add(edge);
                    }

                }
            }
            List<Edge> resuleEdge = new ArrayList<>();
            for (Edge e : handledEdges) {
                if (edgeList.stream().noneMatch(edge -> edge.getTargetNode().equals(e.getSourceNode())
                        && edge.getSourceNode().equals(e.getTargetNode()))) {
                    resuleEdge.add(e);
                }
            }
            map.put(nodeDTOWrapper.wrap(node), edgeDTOWrapper.wrap(resuleEdge));
        }


        GraphDTO graphDTO = new GraphDTO(nodeDTOWrapper.wrap(nodeList), map, appKey);

        List<List<NodeDTO>> recommNodes = new ArrayList<>();
        //寻找测试用例的目标节点集合
        List<Edge> edgesContainsTC = edgeDao.findByAppKeyAndIsCovered(appKey, 1);
        List<Node> nodes = new ArrayList<>();
        for (Edge edge : edgesContainsTC) {
            Node node = nodeDao.findByWindow(edge.getTargetNode());
            if (nodes.stream().noneMatch(node1 -> node1.equals(node))) {
                nodes.add(node);
            }
        }
        //当前节点到目标节点的最短路径
        for (Node n : nodes) {
            if (!n.getWindow().equals(currentWindow)){
                int startIndex = nodeList.indexOf(nodeDao.findByWindow(currentWindow));
                int destIndex = nodeList.indexOf(n);
                List<NodeDTO> nodeDTOs = graphDTO.dijkstraTravasal(startIndex, destIndex);
                //排除不可达节点
                //nodeDTOs.size() == 1 && !nodeDTOs.get(0).equals(currNodeDTO);
                if (nodeDTOs.size() != 1 || nodeDTOs.get(0).equals(currNodeDTO)) {
                    recommNodes.add(nodeDTOs);
                }
            }

        }

        List<Edge> results = new ArrayList<>();

        for (List<NodeDTO> nodePath : recommNodes) {
            String path = "";
            for (NodeDTO nodeDTO : nodePath) {
                path.concat(nodeDTO.getWindow() + " ");
            }
            //排除不可达节点
            if (nodePath.size() != 1 || nodePath.get(0).equals(currNodeDTO)) {
                List<Edge> recommEdges;
                NodeDTO sourceNode;
                NodeDTO targetNode;

                if (nodePath.size() == 1 && nodePath.get(0).equals(currNodeDTO)) {
                    //起始节点自身存在环的情况
                    sourceNode = currNodeDTO;
                    targetNode = currNodeDTO;
                } else {
                    //正常情况
                    sourceNode = nodePath.get(nodePath.size() - 2);
                    targetNode = nodePath.get(nodePath.size() - 1);
                }
                recommEdges = edgeDao.findBySourceNodeAndTargetNodeAndIsCoveredOrderByNumber(
                        sourceNode.getWindow(), targetNode.getWindow(), 1);
                recommEdges.stream().filter(edge -> edge.getNumber() < 0.6 * CROWD_WORKER_NUMBER);
                results.addAll(recommEdges);
            }
        }
        return results;
    }


    @Override
    public List<Edge> getEdgeBySourceNodeAndTargetNode(String sourceNode, String targetNode) {
        return edgeDao.findBySourceNodeAndTargetNodeOrderByNumber(sourceNode, targetNode);
    }

    @Override
    public List<Edge> getRecommActivities(String appKey, String currentWindow) {
        Node node = nodeDao.findByWindow(currentWindow);

        return null;
    }

    @Override
    public List<Edge> getBugEdgeBySourceNodeAndTargetNode(String currentWindow, String window) {
        return edgeDao.findBySourceNodeAndTargetNodeAndIsCoveredOrderByNumber(currentWindow, window, 1);
    }






}
