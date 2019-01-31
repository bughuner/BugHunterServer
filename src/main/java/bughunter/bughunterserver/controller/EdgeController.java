package bughunter.bughunterserver.controller;

import bughunter.bughunterserver.DTO.EdgeDTO;
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
import java.util.stream.Collectors;

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

    @RequestMapping(value = "/pathExtract", method = RequestMethod.GET)
    public String getNodeCoverage() throws Exception {
        String uncoveredPath = "/Users/sean/Desktop/result/666locdemo/pathExtract.txt";
        File uncoveredfile = new File(uncoveredPath);
        if (!uncoveredfile.exists() || uncoveredfile.isDirectory())
            throw new FileNotFoundException();
        BufferedReader br1 = new BufferedReader(new FileReader(uncoveredfile));
        String temp1 = null;
        StringBuffer sb1 = new StringBuffer();
        temp1 = br1.readLine();

        List<EdgeVO> edgeVOs = new ArrayList<>();
        while (temp1 != null) {
            if (temp1.equals("uncovered:")) {
                EdgeVO edgeVO = new EdgeVO();
                edgeVO.setIsCovered(0);

                temp1 = br1.readLine();
                edgeVO.setSourceNode(temp1);

                temp1 = br1.readLine();
                edgeVO.setTargetNode(temp1);

                edgeVO.setCallbacks(br1.readLine());
                edgeVO.setAppKey("666locdemo");
                if (!edgeVO.getCallbacks().equals("[]"))
                    edgeVOs.add(edgeVO);

            }
            temp1 = br1.readLine();
        }

        edgeVOs.stream().distinct().collect(Collectors.toList());

        for (EdgeVO edgeVO : edgeVOs) {
            Edge edge = edgeVOWrapper.unwrap(edgeVO);
            edgeService.save(edge);
        }

        String coveredPath = "/Users/sean/Desktop/result/666locdemo/666_action.txt";
        File covered = new File(coveredPath);
        if (!covered.exists() || covered.isDirectory())
            throw new FileNotFoundException();
        BufferedReader br2 = new BufferedReader(new FileReader(covered));
        String temp2 = null;
        StringBuffer sb = new StringBuffer();
        temp2 = br2.readLine();
        while (temp2 != null) {

            /**
             * {"time":"2018-11-29 14:04:56,102
             * type":"CLICK
             * message":"Click Home button because has tried more than 3 times
             * activityBeforeAction":".LocDemo
             * activityAfterAction":".Launcher"}
             * */
            Log.info(temp2);
            String[] strings = temp2.split("\",\"");
            String activityAfterInfo = strings[strings.length - 1];
            String activityBeforeInfo = strings[strings.length - 2];
            String messageInfo = strings[strings.length - 3];

            String[] stringsAfter = activityAfterInfo.split("\":\"");
            String s1 = stringsAfter[stringsAfter.length - 1];
            String activityAfterAction = s1.substring(1, s1.length() - 2);

            String[] stringsBefore = activityBeforeInfo.split("\":\"");
            String s2 = stringsBefore[stringsBefore.length - 1];
            String activityBeforeAction = s2.substring(1, s2.length());

            String[] stringsMessage = messageInfo.split("\":\"");
            String s3 = stringsMessage[stringsMessage.length - 1];
            String message = s3.substring(0, s3.length());

            EdgeVO edgeVO = new EdgeVO();
            edgeVO.setIsCovered(1);
            edgeVO.setSourceNode(activityBeforeAction);
            edgeVO.setTargetNode(activityAfterAction);
            edgeVO.setCallbacks(message);
            edgeVO.setAppKey("666locdemo");
            Edge edge = edgeVOWrapper.unwrap(edgeVO);
            edgeService.save(edge);
            temp2 = br2.readLine();

        }
        return sb1.toString();
    }

    @RequestMapping(value = "/pathExtractMode", method = RequestMethod.GET)
    public String setCallbacksByAutomaticTool() throws IOException {
        String path = "/Users/sean/Desktop/result/666locdemo/666_action.txt";
        File file = new File(path);
        if (!file.exists() || file.isDirectory())
            throw new FileNotFoundException();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp = null;
        StringBuffer sb = new StringBuffer();
        temp = br.readLine();
        while (temp != null) {

            /**
             * {"time":"2018-11-29 14:04:56,102
             * type":"CLICK
             * message":"Click Home button because has tried more than 3 times
             * activityBeforeAction":".LocDemo
             * activityAfterAction":".Launcher"}
             * */
            Log.info(temp);
            String[] strings = temp.split("\",\"");
            String activityAfterInfo = strings[strings.length - 1];
            String activityBeforeInfo = strings[strings.length - 2];
            String messageInfo = strings[strings.length - 3];

            String[] stringsAfter = activityAfterInfo.split("\":\"");
            String s1 = stringsAfter[stringsAfter.length - 1];
            String activityAfterAction = s1.substring(1, s1.length() - 3);

            String[] stringsBefore = activityAfterInfo.split("\":\"");
            String s2 = stringsAfter[stringsAfter.length - 1];
            String activityBeforeAction = s1.substring(1, s1.length() - 3);

            String[] stringsMessage = activityAfterInfo.split("\":\"");
            String message = stringsAfter[stringsAfter.length - 1];

            EdgeVO edgeVO = new EdgeVO();
            edgeVO.setIsCovered(1);
            edgeVO.setSourceNode(activityBeforeAction);
            edgeVO.setTargetNode(activityAfterAction);
            edgeVO.setCallbacks(message);
            edgeVO.setAppKey("666locdemo");
            Edge edge = edgeVOWrapper.unwrap(edgeVO);
            edgeService.save(edge);

            temp = br.readLine();

        }
        return null;
    }

    @RequestMapping(value = "/path/nextHint/{currentWindow}", method = RequestMethod.POST)
    public Edge getNextBugHint(@PathVariable String currentWindow, @RequestBody List<EdgeDTO> edgeDTOs) {
        return edgeService.getNextBugHint(currentWindow, edgeDTOs);
    }
}
