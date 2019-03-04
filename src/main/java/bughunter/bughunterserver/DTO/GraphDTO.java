package bughunter.bughunterserver.DTO;

import bughunter.bughunterserver.DTO.wrapper.NodeDTOWrapper;
import bughunter.bughunterserver.dao.NodeDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author sean
 * @date 2019-01-30.
 */
public class GraphDTO {

    private List<NodeDTO> nodeDTOs;   //图的顶点集
    private HashMap<NodeDTO, List<EdgeDTO>> node_edgeList_map;  //图的每个顶点对应的有向边
    private String appKey;

    public GraphDTO(List<NodeDTO> nodeDTOs, HashMap<NodeDTO, List<EdgeDTO>> node_edgeList_map, String appKey) {
        super();
        this.nodeDTOs = nodeDTOs;
        this.node_edgeList_map = node_edgeList_map;
        this.appKey = appKey;
    }

    public List<NodeDTO> getNodeDTOs() {
        return nodeDTOs;
    }

    public void setNodeDTOs(List<NodeDTO> nodeDTOs) {
        this.nodeDTOs = nodeDTOs;
    }


    public HashMap<NodeDTO, List<EdgeDTO>> getNode_edgeList_map() {
        return node_edgeList_map;
    }

    public void setNode_edgeList_map(HashMap<NodeDTO, List<EdgeDTO>> node_edgeList_map) {
        this.node_edgeList_map = node_edgeList_map;
    }


    @Autowired
    NodeDao nodeDao;
    @Autowired
    NodeDTOWrapper nodeDTOWrapper;

    public List<NodeDTO> getNodeDTOList() {
        return nodeDTOs;
    }

    public void setNodeDTOList(List<NodeDTO> nodeDTOList) {
        this.nodeDTOs = nodeDTOList;
    }

    public Map<NodeDTO, List<EdgeDTO>> getVer_edgeList_map() {
        return node_edgeList_map;
    }

    public void setVer_edgeList_map(HashMap<NodeDTO, List<EdgeDTO>> ver_edgeList_map) {
        this.node_edgeList_map = ver_edgeList_map;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    //设置起始节点
    public void setRoot(NodeDTO v) {
        v.setParent(null);
        v.setAdjuDist(0);
    }

    /**
     * @param startIndex dijkstra遍历的起点节点下标
     * @param destIndex  dijkstra遍历的终点节点下标
     */
    public List<NodeDTO> dijkstraTravasal(int startIndex, int destIndex) {
        NodeDTO start = nodeDTOs.get(startIndex);
        NodeDTO dest = nodeDTOs.get(destIndex);
        String path = "[" + dest.getWindow() + "]";

        setRoot(start);
        updateChildren(nodeDTOs.get(startIndex));

        int shortest_length = dest.getAdjuDist();

        List<NodeDTO> nodeDTOList = new ArrayList<>();
        while ((dest.getParent() != null) && (!dest.equals(start))) {
            path = "[" + dest.getParent().getWindow() + "] --> " + path;
            nodeDTOList.add(dest.getParent());
            dest = dest.getParent();
        }

        System.out.println("[" + nodeDTOs.get(startIndex).getWindow() + "] to [" +
                nodeDTOs.get(destIndex).getWindow() + "] dijkstra shortest path :: " + path);
        System.out.println("shortest length::" + shortest_length);
        return nodeDTOList;
    }

    public List<NodeDTO> dijkstraTravasal(NodeDTO start, NodeDTO dest) {
        List<NodeDTO> nodeDTOs = new ArrayList<>();
        nodeDTOs.add(dest);
        String path = "[" + dest.getWindow() + "]";

        setRoot(start);
        updateChildren(start);

        int shortest_length = dest.getAdjuDist();

        while ((dest.getParent() != null) && (!dest.equals(start))) {
            path = "[" + dest.getParent().getWindow() + "] --> " + path;
            dest = dest.getParent();
            nodeDTOs.add(dest);
        }

        System.out.println("[" + start.getWindow() + "] to [" +
                dest.getWindow() + "] dijkstra shortest path :: " + path);
        System.out.println("shortest length::" + shortest_length);
        return nodeDTOs;
    }

    /**
     * 从初始节点开始递归更新邻接表
     *
     * @param n
     */
    private void updateChildren(NodeDTO n) {
        if (n == null) {
            return;
        }

        if (node_edgeList_map.get(n) == null || node_edgeList_map.get(n).size() == 0) {
            return;
        }
        //用来保存每个可达的节点
        List<NodeDTO> childrenList = new LinkedList<>();
        for (EdgeDTO e : node_edgeList_map.get(n)) {
//            Vertex childVertex = e.getEndVertex();
            NodeDTO nodeDTO = nodeDTOWrapper.wrap(nodeDao.findByWindow(e.getTargetNode()));

            //如果子节点之前未知，则进行初始化，
            //把当前边的开始点默认为子节点的父节点，长度默认为边长加边的起始节点的长度，并修改该点为已经添加过，表示不用初始化
            if (!nodeDTO.isKnown()) {
                nodeDTO.setKnown(true);
                nodeDTO.setAdjuDist(n.getAdjuDist() + e.getWeight());
                nodeDTO.setParent(n);
                childrenList.add(nodeDTO);
            }

            //此时该子节点的父节点和之前到该节点的最小长度已经知道了，则比较该边起始节点到该点的距离是否小于子节点的长度，
            //只有小于的情况下，才更新该点为该子节点父节点,并且更新长度。
            int nowDist = n.getAdjuDist() + e.getWeight();
            if (nowDist >= nodeDTO.getAdjuDist()) {
                continue;
            } else {
                nodeDTO.setAdjuDist(nowDist);
                nodeDTO.setParent(n);
            }
        }

        //更新每一个子节点
        for (NodeDTO vc : childrenList) {
            updateChildren(vc);
        }
    }


}
