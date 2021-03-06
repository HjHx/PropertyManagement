package servlet;

import entity.Device;
import exception.DeviceException;
import manage.DeviceManage;
import manage.impl.DeviceManageImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 我的袜子都是洞
 * @description:
 * @path: PropertyManagement-servlet-DoUpdateDevice
 * @date: 2019-04-30 23:38
 */
@WebServlet("/doupdatedevice")
public class DoUpdateDevice extends HttpServlet {
    private DeviceManage deviceManage = new DeviceManageImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int usertype = (int)req.getSession().getAttribute("usertype");
        String operator = (String) (req.getSession().getAttribute("loginname"));
        int did;
        String device_name = "";
        String device_type = "";
        String processing_opinion = "";
        int is_service = -1;
        String handlers = "";

        device_name = req.getParameter("device_name");
        device_type = req.getParameter("device_type");
        did = Integer.parseInt(req.getParameter("did"));
        is_service = Integer.parseInt(req.getParameter("is_service"));
        processing_opinion = req.getParameter("processing_opinion");
        handlers = req.getParameter("handlers");

        if ("".equals(device_name) ||
                "".equals(device_type) ||
                "".equals(processing_opinion) ||
                is_service<0 ||
                "".equals(handlers)
        ) {
            req.setAttribute("title", "添加设备信息失败");
            req.setAttribute("detail", "信息填写不完整");
            req.getRequestDispatcher("/comm/error.jsp").forward(req, resp);
            return;
        }

        Device device = new Device();
        device.setDid(did);
        device.setDevice_name(device_name);
        device.setDevice_type(device_type);
        device.setIs_service(is_service);
        device.setProcessing_opinion(processing_opinion);
        device.setHandlers(handlers);

        if (usertype != 2) {
            req.setAttribute("title", "权限不足");
            req.setAttribute("detail", "仅物业管理员可操作");
            req.getRequestDispatcher("/comm/error.jsp").forward(req, resp);
        } else {
            try {
                boolean flag = deviceManage.updateDevice(device, operator);
                if (flag) {
                    req.getRequestDispatcher("device_list.jsp").forward(req, resp);
                } else {
                    req.setAttribute("title", "添加设备信息失败");
                    req.setAttribute("detail", "暂无");
                    req.getRequestDispatcher("/comm/error.jsp").forward(req, resp);
                }
            } catch (DeviceException e) {
                req.setAttribute("title", "添加设备信息失败");
                req.setAttribute("detail", e.getMessage());
                req.getRequestDispatcher("/comm/error.jsp").forward(req, resp);
            }
        }
    }
}
