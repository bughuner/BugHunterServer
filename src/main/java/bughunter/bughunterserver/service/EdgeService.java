package bughunter.bughunterserver.service;

import bughunter.bughunterserver.DTO.EdgeDTO;
import bughunter.bughunterserver.model.entity.Edge;

import java.util.List;

/**
 * @author sean
 * @date 2019-01-23.
 */

public interface EdgeService {

    Edge save(Edge edge);

    Edge getNextBugHint(String currentWindow, List<EdgeDTO> edgeDTOs);

    List<Edge> getRecommBugs(String appKey, String currentWindow);

    List<Edge> getEdgeBySourceNodeAndTargetNode(String sourceNode, String targetNode);
}
