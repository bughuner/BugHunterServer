package bughunter.bughunterserver.service;

import bughunter.bughunterserver.model.entity.Edge;

import java.util.List;

/**
 * @author sean
 * @date 2019-01-23.
 */

public interface EdgeService {

    Edge save(Edge edge);

    Edge getNextBugHint(String currentWindow, String nextWindow);

    List<String> getRecommBugs(String appKey, String currentWindow, Integer isCovered);

    List<Edge> getEdgeBySourceNodeAndTargetNode(String sourceNode, String targetNode);

    List<Edge> getRecommActivities(String appKey, String currentWindow);

    List<Edge> getBugEdgeBySourceNodeAndTargetNode(String currentWindow, String window);
}
