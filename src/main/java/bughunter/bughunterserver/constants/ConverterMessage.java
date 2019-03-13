package bughunter.bughunterserver.constants;

/**
 * @author sean
 * @date 2019-03-08.
 */
public class ConverterMessage {

    public static String convertMessage(String message) {
        if (message.equals("Click Home button because has tried more than 3 times")) {
            return "多次返回无响应,回到桌面";
        } else if (message.equals("Click Return button because this page has done")) {
            return "页面结束,按返回键";
        } else if (message.contains("_") && !message.contains("implicit")) {
            String[] ms = message.split("\\/");
            String[] ms2 = ms[1].split("\\, ");
            message = ms2[0];
            StringBuffer stringBuffer = new StringBuffer("点击");
            if (message.split("\\_").length != 0) {

                if (message.contains("_fab"))
                    stringBuffer.append("悬浮按钮");
                else if (message.contains("card"))
                    stringBuffer.append("物品收藏卡片");
                String[] parts = message.split("\\_");

                for (int i = 0; i < parts.length; i++) {
                    switch (parts[i]) {
                        case "toolbar":
                            stringBuffer.append("顶部");
                            break;
                        case "menu":
                            stringBuffer.append("菜单");
                            break;
                        case "search":
                            stringBuffer.append("搜索");
                            break;
                        case "button":
                            stringBuffer.append("按钮");
                            break;
                        case "btn":
                            stringBuffer.append("按钮");
                            break;
                        case "close":
                            stringBuffer.append("关闭");
                            break;
                        case "tv":
                            stringBuffer.append("文本框");
                            break;
                        case "edit":
                            stringBuffer.append("编辑");
                    }
                }

            }
            return stringBuffer.toString();
        } else if (message.contains("contains")) {
            StringBuffer stringBuffer = new StringBuffer("请点击");
            if (message.contains("ImageButton"))
                stringBuffer.append("图片按钮");
            if (message.contains("FrameLayout"))
                stringBuffer.append("帧布局");
            if (message.contains("LinearLayoutCompat"))
                stringBuffer.append("线性布局");
            if (message.contains("TextView"))
                stringBuffer.append("文本框");

            return stringBuffer.toString();
        } else if (message.equals("click")) {
            message = "尚待探索的页面";
        } else if (message.equals("implicit_back_event")){
            message = "返回按钮";
        }else if (message.equals("implicit_power_event")){
            message = "低电量模式测试";
        }else if (message.equals("implicit_launch_event")){
            message = "回到手机主页";
        } else {
            return message;
        }
        return message;
    }


}
