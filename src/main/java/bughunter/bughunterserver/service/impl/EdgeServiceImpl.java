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

/**
 * @author sean
 * @date 2019-01-23.
 */
@Service
public class EdgeServiceImpl implements EdgeService {

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


    public void quickSort(List<List<NodeDTO>> nodeDTOs) {
        Qsort(nodeDTOs, 1, nodeDTOs.size() - 1);
    }

    public void Qsort(List<List<NodeDTO>> NodeDTOs, int low, int high) {
        int pivot;
        if (low < high) {
            //将L[low,high]一分为二,算出枢轴值pivot,该值得位置固定,不用再变化
            pivot = partition0(NodeDTOs, low, high);

            //对两边的数组分别排序
            Qsort(NodeDTOs, low, pivot - 1);
            Qsort(NodeDTOs, pivot + 1, high);
        }
    }

    //  选择一个枢轴值(关键字) 把它放到某个位置 使其左边的值都比它小 右边的值都比它大
    public int partition0(List<List<NodeDTO>> nodeDTOs, int low, int high) {
        int pivotkey;
        pivotkey = nodeDTOs.get(low).size();
        //顺序很重要，要先从右边找
        while (low < high) {
            while (low < high && nodeDTOs.get(high).size() >= pivotkey) {  //从后往前找到比key小的放到前面去
                high--;
            }
            swap(nodeDTOs, low, high);
            while (low < high && nodeDTOs.get(low).size() <= pivotkey) {  //从前往后找到比key大的 放到后面去
                low++;
            }
            swap(nodeDTOs, low, high);
        } //遍历所有记录  low的位置即为 key所在位置, 且固定,不用再改变
        return low;
    }

    //交换数组的两个位置
    public void swap(List<List<NodeDTO>> nodeDTOs, int i, int j) {
        List<NodeDTO> temp = nodeDTOs.get(i);
        nodeDTOs.set(i, nodeDTOs.get(j));
        nodeDTOs.set(j, temp);
    }


    @Override
    public Edge getNextBugHint(String currentWindow, List<EdgeDTO> edgeDTOs) {
        for (int i = 0; i < edgeDTOs.size() - 1; i++) {
            if (edgeDTOs.get(i).getSourceNode().equals(currentWindow)) {
                return edgeDTOWrapper.unwrap(edgeDTOs.get(i - 1));
            }
        }
        return null;
    }

    @Override
    public List<List<NodeDTO>> getRecommBugs(String appKey, String currentWindow) {
        NodeDTO nodeDTO = nodeDTOWrapper.wrap(nodeDao.findByWindow(currentWindow));
        List<Edge> edges = edgeDao.findByAppKey(appKey);

        List<Node> nodes = new ArrayList<>();
        HashMap<NodeDTO, List<EdgeDTO>> map = new HashMap<>();
        for (Edge e : edges) {
            List<Edge> edgeList = new ArrayList<>();
            Node n = nodeDao.findByWindow(e.getSourceNode());
            if (!nodes.contains(n)) {
                edgeList = edgeDao.findBySourceNode(n.getWindow());
                nodes.add(n);
                map.put(nodeDTOWrapper.wrap(n), edgeDTOWrapper.wrap(edgeList));
            }

//                if (edgeList.stream().noneMatch(edge -> edge.getSourceNode().equals(e.getSourceNode()))
//                        && edgeList.stream().noneMatch(edge -> edge.getTargetNode().equals(e.getTargetNode()))
//                        && n != null) {
//                    edgeList.add(e);
//                    if ()
//                        nodes.add(n);
//                }
//            if (n != null) {
//                map.put(nodeDTOWrapper.wrap(n), edgeDTOWrapper.wrap(edgeList));
//            }

        }

        GraphDTO graphDTO = new GraphDTO(nodeDTOWrapper.wrap(nodes), map, appKey);

        List<List<NodeDTO>> recommNodes = new ArrayList<>();
        for (Node n : nodes) {
            List<NodeDTO> nodeDTOs = graphDTO.dijkstraTravasal(nodeDTO, nodeDTOWrapper.wrap(n));
            recommNodes.add(nodeDTOs);
        }

        List<List<NodeDTO>> result = sort(recommNodes, nodeDTO);
//        quickSort(recommNodes);

        return result;

//        List<List<Edge>> result = new ArrayList<>();
//        List<Edge> edgeList = new ArrayList<>();
//        for (List<NodeDTO> nodeDTOs : recommNodes) {
//            Node sourceNode = null;
//            Node targetNode = null;
//            if (nodeDTOs.size() < 2) {
//                sourceNode = nodeDTOWrapper.unwrap(nodeDTO);
//                targetNode = nodeDTOWrapper.unwrap(nodeDTOs.get(0));
//            } else {
//                sourceNode = nodeDTOWrapper.unwrap(nodeDTOs.get(nodeDTOs.size() - 2));
//                targetNode = nodeDTOWrapper.unwrap(nodeDTOs.get(nodeDTOs.size() - 1));
//            }
//
//            List<Edge> es = edgeDao.findBySourceNodeAndTargetNode(sourceNode.getWindow(), targetNode.getWindow());
//            if (es.size() != 1 || es.get(0).getIsCovered() != 0)
//                //只添加自动化测试工具得出的异常边
//                edgeList.addAll(es);
//        }
//        return edgeList.stream().distinct().collect(Collectors.toList());
    }

    private List<List<NodeDTO>> sort(List<List<NodeDTO>> recommNodes, NodeDTO nodeDTO) {
        List<NodeDTO> temp = new ArrayList<>();
        for (int i = 0; i < recommNodes.size() - 1; i++) {
            for (int j = i + 1; j < recommNodes.size() - i - 1; j++) {
                if (recommNodes.get(j).size() == 1
                        && (recommNodes.get(j).get(0).getWindow().equals(nodeDTO.getWindow()))) {
                    temp = recommNodes.get(0);
                    recommNodes.set(0, recommNodes.get(j));
                    recommNodes.set(j, temp);
                }
                if (recommNodes.get(j + 1).size() < recommNodes.get(j).size()) {
                    temp = recommNodes.get(j);
                    recommNodes.set(j, recommNodes.get(j + 1));
                    recommNodes.set(j + 1, temp);
                }
            }
        }
        return recommNodes;

    }

    @Override
    public List<Edge> getEdgeBySourceNodeAndTargetNode(String sourceNode, String targetNode) {
        return edgeDao.findBySourceNodeAndTargetNode(sourceNode, targetNode);
    }

    @Override
    public List<List<NodeDTO>> getRecommActivities(String appKey, String currentWindow) {
        Node node = nodeDao.findByWindow(currentWindow);

        return null;
    }

    @Override
    public List<Edge> getBugEdgeBySourceNodeAndTargetNode(String currentWindow, String window) {
        return edgeDao.findBySourceNodeAndTargetNodeAndIsCovered(currentWindow, window, 1);
    }
}
