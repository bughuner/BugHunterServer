package bughunter.bughunterserver.controller;

import bughunter.bughunterserver.model.entity.Edge;
import bughunter.bughunterserver.service.EdgeService;
import bughunter.bughunterserver.service.NodeService;
import bughunter.bughunterserver.vo.EdgeVO;
import bughunter.bughunterserver.wrapper.EdgeVOWrapper;
import com.oracle.tools.packager.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sean
 * @date 2019-01-26.
 */
@RestController
public class EdgeController {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private EdgeVOWrapper edgeVOWrapper;

    @Autowired
    private EdgeService edgeService;


    @RequestMapping(value = "/WTG", method = RequestMethod.GET)
    public String getNodeCoverage() throws Exception {
        //插入覆盖的边
        String coveredPath = "/Users/sean/Desktop/Cheers/action.txt";
        File covered = new File(coveredPath);
        if (!covered.exists() || covered.isDirectory())
            throw new FileNotFoundException();
        BufferedReader br2 = new BufferedReader(new FileReader(covered));
        String temp2 = null;
        StringBuffer sb = new StringBuffer();
        temp2 = br2.readLine();
        while (temp2 != null) {

            /**
             * 0{"time":"2018-11-29 14:04:56,102
             * 1type":"CLICK
             * 2message":"Click Home button because has tried more than 3 times
             * 3activityBeforeAction":".activity.MainActivity
             * 4activityAfterAction":".Launcher"}
             * */
            Log.info(temp2);
            String[] strings = temp2.split("\",\"");
            String activityAfterInfo = strings[4];
            String activityBeforeInfo = strings[3];
            String messageInfo = strings[2];

            String[] stringsAfter = activityAfterInfo.split("\":\"");
            String s1 = stringsAfter[stringsAfter.length - 1];
            //.activity.MainActivity
            //0123456789
            String activityAfterAction = s1.substring(10, s1.length());

            String[] stringsBefore = activityBeforeInfo.split("\":\"");
            String s2 = stringsBefore[stringsBefore.length - 1];
            String activityBeforeAction = s2.substring(10, s2.length());

            String[] stringsMessage = messageInfo.split("\":\"");
            String s3 = stringsMessage[stringsMessage.length - 1];
            String message = s3.substring(0, s3.length());

            EdgeVO edgeVO = new EdgeVO();
            edgeVO.setSourceNode(activityBeforeAction);
            edgeVO.setTargetNode(activityAfterAction);
            edgeVO.setEventHandlers(message);
            edgeVO.setEventType("click");
            edgeVO.setAppKey("JianDou");
            edgeVO.setNumber(0);
            edgeVO.setIsCovered(1);

            Edge e = edgeVOWrapper.unwrap(edgeVO);
            edgeService.save(e);
            temp2 = br2.readLine();
        }

        String uncoveredPath = "/Users/sean/Desktop/Cheers/graph.txt";
        File uncoveredfile = new File(uncoveredPath);
        if (!uncoveredfile.exists() || uncoveredfile.isDirectory())
            throw new FileNotFoundException();
        BufferedReader br1 = new BufferedReader(new FileReader(uncoveredfile));
        String temp1 = null;
        StringBuffer sb1 = new StringBuffer();
        temp1 = br1.readLine();

        List<EdgeVO> edgeVOs = new ArrayList<>();
        while (temp1 != null) {
            if (temp1.equals("edge:")) {
                EdgeVO edgeVO = new EdgeVO();
                edgeVO.setIsCovered(0);

                temp1 = br1.readLine();
                edgeVO.setSourceNode(temp1);

                temp1 = br1.readLine();
                edgeVO.setTargetNode(temp1);

                List<Edge> edges = edgeService.getEdgeBySourceNodeAndTargetNode(edgeVO.getSourceNode(), edgeVO.getTargetNode());

                if (edges.size() == 0) {
                    edgeVO.setEventHandlers(br1.readLine());
                    edgeVO.setEventType(br1.readLine());
                    edgeVO.setAppKey("JianDou");
                    edgeVO.setNumber(0);
                    edgeVO.setIsCovered(0);
                    edgeService.save(edgeVOWrapper.unwrap(edgeVO));
                }
            }
            temp1 = br1.readLine();
        }


        return sb1.toString();
    }

    @RequestMapping(value = "/path/nextHint/{currentWindow}/{nextWindow}", method = RequestMethod.POST)
    public Edge getNextBugHint(@PathVariable String currentWindow, @PathVariable String nextWindow) {
        String[] infos = currentWindow.split("\\.");
        currentWindow = infos[infos.length-1];
        return edgeService.getNextBugHint(currentWindow, nextWindow);
    }
}
