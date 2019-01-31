package bughunter.bughunterserver.DTO.wrapper;

import bughunter.bughunterserver.DTO.NodeDTO;
import bughunter.bughunterserver.model.entity.Node;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @date 2019-01-26.
 */
@Service
public class NodeDTOWrapper extends BaseDTOWrapper<NodeDTO, Node> {
    @Override
    public NodeDTO wrap(Node node) {
        NodeDTO nodeDTO = new NodeDTO();
        nodeDTO.setId(node.getId());
        nodeDTO.setAppKey(node.getAppKey());
        nodeDTO.setWindow(node.getWindow());

        nodeDTO.setAdjuDist(0);
        nodeDTO.setKnown(false);
        nodeDTO.setParent(null);
        return nodeDTO;
    }

    @Override
    public Node unwrap(NodeDTO data) {
        Node node = new Node();
        node.setId(data.getId());
        node.setWindow(data.getWindow());
        node.setAppKey(data.getAppKey());
        return node;
    }
}
