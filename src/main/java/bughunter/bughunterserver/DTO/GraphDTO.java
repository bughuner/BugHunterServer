package bughunter.bughunterserver.DTO;

import bughunter.bughunterserver.DTO.wrapper.NodeDTOWrapper;
import bughunter.bughunterserver.dao.NodeDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author sean
 * @date 2019-01-30.
 */
public class GraphDTO {

    @Autowired
    NodeDao nodeDao;
    @Autowired
    NodeDTOWrapper nodeDTOWrapper;

    private String appKey;
    private List<NodeDTO> nodeDTOList;   //图的顶点集
    private Map<NodeDTO, List<EdgeDTO>> ver_edgeList_map;  //图的每个顶点对应的有向边

    public GraphDTO(List<NodeDTO> nodeDTOList, Map<NodeDTO, List<EdgeDTO>> ver_edgeList_map, String appKey) {
        super();
        this.nodeDTOList = nodeDTOList;
        this.ver_edgeList_map = ver_edgeList_map;
        this.appKey = appKey;
    }

    public List<NodeDTO> getNodeDTOList() {
        return nodeDTOList;
    }

    public void setNodeDTOList(List<NodeDTO> nodeDTOList) {
        this.nodeDTOList = nodeDTOList;
    }

    public Map<NodeDTO, List<EdgeDTO>> getVer_edgeList_map() {
        return ver_edgeList_map;
    }

    public void setVer_edgeList_map(Map<NodeDTO, List<EdgeDTO>> ver_edgeList_map) {
        this.ver_edgeList_map = ver_edgeList_map;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setRoot(NodeDTO v) {
        v.setParent(null);
        v.setAdjuDist(0);
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

        if (ver_edgeList_map.get(n) == null || ver_edgeList_map.get(n).size() == 0) {
            return;
        }
        //用来保存每个可达的节点
        List<NodeDTO> childrenList = new LinkedList<>();
        for (EdgeDTO e : ver_edgeList_map.get(n)) {
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
