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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<List<Edge>> getRecommendedBugs(String appKey, String currentWindow) {
        Node currentNode = nodeDao.findByWindow(currentWindow);

        List<Edge> edges = edgeDao.findByAppKeyAndIsCovered(appKey, 1);
        //自动化工具下 所有会触发bug的source window
        List<String> sourceNodes = edges.stream().map(edge -> edge.getSourceNode()).collect(Collectors.toList());
        //去重
        List<String> sourceNodesList = sourceNodes.stream().distinct().collect(Collectors.toList());

        Map<NodeDTO, List<EdgeDTO>> map = null;
        List<NodeDTO> coveredNodes = new ArrayList<>();

        for (String nodeWindow : sourceNodesList) {
            Node node = nodeDao.findByWindow(nodeWindow);
            coveredNodes.add(nodeDTOWrapper.wrap(node));
            List<Edge> edgeList = edgeDao.findByAppKeyAndSourceNode(appKey, node.getWindow());
            edgeList.addAll(edgeDao.findByAppKeyAndTargetNode(appKey, node.getWindow()));
            map.put(nodeDTOWrapper.wrap(node), edgeDTOWrapper.wrap(edgeList));
        }

        GraphDTO graphDTO = new GraphDTO(coveredNodes, map, appKey);
        //获取当前页面到其它存在Bug页面的最短距离
        List<List<Edge>> shortestEdges = new ArrayList<>();
        for (NodeDTO node : coveredNodes) {
            List<NodeDTO> nodes = graphDTO.dijkstraTravasal(nodeDTOWrapper.wrap(currentNode), node);
            List<Edge> edgeList = new ArrayList<>();

            for (int i = 0; i < nodes.size() - 1; i++) {
                List<Edge> coverEdges = edgeDao.findBySourceNodeAndTargetNode(nodes.get(i).getWindow(), nodes.get(i + 1).getWindow());
                edgeList.add(coverEdges.get(0));
            }
            shortestEdges.add(edgeList);
        }
        //根据距离排序
        quickSort(shortestEdges);

        return shortestEdges;
    }


    public void quickSort(List<List<Edge>> edges) {
        Qsort(edges, 1, edges.size() - 1);
    }

    public void Qsort(List<List<Edge>> edges, int low, int high) {
        int pivot;
        if (low < high) {
            //将L[low,high]一分为二,算出枢轴值pivot,该值得位置固定,不用再变化
            pivot = partition0(edges, low, high);

            //对两边的数组分别排序
            Qsort(edges, low, pivot - 1);
            Qsort(edges, pivot + 1, high);
        }
    }

    //  选择一个枢轴值(关键字) 把它放到某个位置 使其左边的值都比它小 右边的值都比它大
    public int partition0(List<List<Edge>> edges, int low, int high) {
        int pivotkey;
        pivotkey = edges.get(low).size();
        //顺序很重要，要先从右边找
        while (low < high) {
            while (low < high && edges.get(high).size() >= pivotkey) {  //从后往前找到比key小的放到前面去
                high--;
            }
            swap(edges, low, high);
            while (low < high && edges.get(low).size() <= pivotkey) {  //从前往后找到比key大的 放到后面去
                low++;
            }
            swap(edges, low, high);
        } //遍历所有记录  low的位置即为 key所在位置, 且固定,不用再改变
        return low;
    }

    //交换数组的两个位置
    public void swap(List<List<Edge>> edges, int i, int j) {
        List<Edge> temp = edges.get(i);
        edges.set(i, edges.get(j));
        edges.set(j, temp);
    }

    @Override
    public Edge getEdgeBySourceNodeAndTargetNodeAndCallbacksAndAppkey(String sourceNode, String targetNode, String callbacks, String appKey) {
        return edgeDao.findBySourceNodeAndTargetNodeAndCallbacksAndAppKey(sourceNode, targetNode, callbacks, appKey);
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
}
