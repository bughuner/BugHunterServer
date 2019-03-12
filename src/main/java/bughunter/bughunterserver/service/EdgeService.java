package bughunter.bughunterserver.service;

import bughunter.bughunterserver.model.entity.Edge;
import bughunter.bughunterserver.vo.EdgeVO;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author sean
 * @date 2019-01-23.
 */

public interface EdgeService {

    Edge save(Edge edge);

    Edge getNextBugHint(String currentWindow, String nextWindow);

    List<EdgeVO> getRecommBugs(String appKey, String currentWindow, Integer isCovered, Integer userId);

    List<Edge> getEdgeBySourceNodeAndTargetNode(String sourceNode, String targetNode);

    List<Edge> getRecommActivities(String appKey, String currentWindow);

    List<Edge> getBugEdgeBySourceNodeAndTargetNode(String currentWindow, String window);

    Edge updateEdge(Long id);

    Edge getEdgesByCreateTime(Timestamp createTime);

    Edge getEdgeByCreateTimeAndAssistTime(Timestamp createTime, int standard);

    List<Edge> getEdgesByAppKey(String jianDou);
}
