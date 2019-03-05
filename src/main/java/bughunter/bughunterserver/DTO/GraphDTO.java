package bughunter.bughunterserver.DTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author sean
 * @date 2019-01-30.
 */
public class GraphDTO {

    private List<NodeDTO> nodeDTOs;   //图的顶点集
    private HashMap<NodeDTO, List<EdgeDTO>> node_edgeList_map;  //图的每个顶点对应的有向边
    private String appKey;

    public GraphDTO(List<NodeDTO> vertexList, HashMap<NodeDTO, List<EdgeDTO>> ver_edgeList_map, String appKey) {
        super();
        this.nodeDTOs = vertexList;
        this.appKey = appKey;
        this.node_edgeList_map = ver_edgeList_map;
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

    public List<NodeDTO> dijkstraTravasal(int startIndex, int destIndex) {
        NodeDTO start = nodeDTOs.get(startIndex);
        NodeDTO dest = nodeDTOs.get(destIndex);

        String path = "[" + dest.getWindow() + "]";

        setRoot(start);
        updateChildren(nodeDTOs.get(startIndex));

        int shortest_length = dest.getAdjuDist();

        List<NodeDTO> result = new ArrayList<>();
        result.add(start);
        while ((dest.getParent() != null) && (!dest.equals(start))) {
            result.add(dest);
            path = "[" + dest.getParent().getWindow() + "] --> " + path;
            dest = dest.getParent();
        }

        System.out.println("[" + start.getWindow() + "] to [" +
                dest.getWindow() + "] dijkstra shortest path :: " + path);
        System.out.println("shortest length::" + shortest_length);
        return result;
    }

    /**
     * 从初始节点开始递归更新邻接表
     *
     * @param v
     */
    private void updateChildren(NodeDTO v) {
        if (v == null) {
            return;
        }

        if (node_edgeList_map.get(v) == null || node_edgeList_map.get(v).size() == 0) {
            return;
        }
        //用来保存每个可达的节点
        List<NodeDTO> childrenList = new LinkedList<NodeDTO>();
        for (EdgeDTO e : node_edgeList_map.get(v)) {
            NodeDTO nodeDTO = e.getTargetNode();

            //如果子节点之前未知，则进行初始化，
            //把当前边的开始点默认为子节点的父节点，长度默认为边长加边的起始节点的长度，并修改该点为已经添加过，表示不用初始化
            if (!nodeDTO.isKnown()) {
                nodeDTO.setKnown(true);
                nodeDTO.setAdjuDist(v.getAdjuDist() + e.getWeight());
                nodeDTO.setParent(v);
                childrenList.add(nodeDTO);
            }

            //此时该子节点的父节点和之前到该节点的最小长度已经知道了，则比较该边起始节点到该点的距离是否小于子节点的长度，
            //只有小于的情况下，才更新该点为该子节点父节点,并且更新长度。
            int nowDist = v.getAdjuDist() + e.getWeight();
            if (nowDist >= nodeDTO.getAdjuDist()) {
                continue;
            } else {
                nodeDTO.setAdjuDist(nowDist);
                nodeDTO.setParent(v);
            }
        }

        //更新每一个子节点
        for (NodeDTO vc : childrenList) {
            updateChildren(vc);
        }
    }

}
