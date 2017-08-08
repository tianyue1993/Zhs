package com.zhs.zhs.activity.device.detail;

import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.activity.device.detail.CombustibleGas.CombustibleBase;
import com.zhs.zhs.activity.device.detail.CombustibleGas.CombustibleMine;
import com.zhs.zhs.activity.device.detail.CombustibleGas.CombustibleNotice;
import com.zhs.zhs.activity.device.detail.CombustibleGas.CombustibleScence;
import com.zhs.zhs.activity.device.detail.Concentrator.ConcentratorBase;
import com.zhs.zhs.activity.device.detail.Concentrator.ConcentratorMine;
import com.zhs.zhs.activity.device.detail.Concentrator.ConcentratorNotice;
import com.zhs.zhs.activity.device.detail.Concentrator.ConcentratorScence;
import com.zhs.zhs.activity.device.detail.Curtain.CurtainBase;
import com.zhs.zhs.activity.device.detail.Curtain.CurtainMine;
import com.zhs.zhs.activity.device.detail.Curtain.CurtainNotice;
import com.zhs.zhs.activity.device.detail.Curtain.CurtainScence;
import com.zhs.zhs.activity.device.detail.DoorAlarm.DoorAlarmBase;
import com.zhs.zhs.activity.device.detail.DoorAlarm.DoorAlarmMine;
import com.zhs.zhs.activity.device.detail.DoorAlarm.DoorAlarmNotice;
import com.zhs.zhs.activity.device.detail.DoorAlarm.DoorAlarmScence;
import com.zhs.zhs.activity.device.detail.Electric.ElectricBase;
import com.zhs.zhs.activity.device.detail.Electric.EletricMine;
import com.zhs.zhs.activity.device.detail.Electric.EletricNotice;
import com.zhs.zhs.activity.device.detail.Electric.EletricScence;
import com.zhs.zhs.activity.device.detail.Gas.GasBase;
import com.zhs.zhs.activity.device.detail.Gas.GasMine;
import com.zhs.zhs.activity.device.detail.Gas.GasNotice;
import com.zhs.zhs.activity.device.detail.Gas.GasScence;
import com.zhs.zhs.activity.device.detail.GlassShock.GlassShockBase;
import com.zhs.zhs.activity.device.detail.GlassShock.GlassShockMine;
import com.zhs.zhs.activity.device.detail.GlassShock.GlassShockNotice;
import com.zhs.zhs.activity.device.detail.GlassShock.GlassShockScence;
import com.zhs.zhs.activity.device.detail.SmartPlug.SmartPlugBase;
import com.zhs.zhs.activity.device.detail.SmartPlug.SmartPlugMine;
import com.zhs.zhs.activity.device.detail.SmartPlug.SmartPlugNotice;
import com.zhs.zhs.activity.device.detail.SmartPlug.SmartPlugScence;
import com.zhs.zhs.activity.device.detail.SmartSwitch.SmartSwitchBase;
import com.zhs.zhs.activity.device.detail.SmartSwitch.SmartSwitchMine;
import com.zhs.zhs.activity.device.detail.SmartSwitch.SmartSwitchNotice;
import com.zhs.zhs.activity.device.detail.SmartSwitch.SmartSwitchScence;
import com.zhs.zhs.activity.device.detail.Water.WaterBase;
import com.zhs.zhs.activity.device.detail.Water.WaterMine;
import com.zhs.zhs.activity.device.detail.Water.WaterNotice;
import com.zhs.zhs.activity.device.detail.Water.WaterScence;

/**
 * Created by admin on 2017/7/26.
 */

public class DeviceContext {

    public static void Initial(DeviceBaseInterface deviceBaseInterface, String clientId, String areaId) {
        Initial(deviceBaseInterface, clientId, areaId, clientId, clientId);
    }

    public static void Initial(DeviceBaseInterface deviceBaseInterface, String clientId, String areaId, String key, String value) {
        deviceBaseInterface.setTitle();
        deviceBaseInterface.setRightText();
        deviceBaseInterface.getDetail(clientId);
        deviceBaseInterface.reName(areaId);
        deviceBaseInterface.registerReceiver();
        deviceBaseInterface.backEvent();
        deviceBaseInterface.startPush(key, value);
    }


    public static GasBase getGasBaseInstance(String type, Baseactivity baseactivity) {
        GasBase result;
        if ("1".equals(type)) {
            result = new GasScence(baseactivity);
            result.title = "场景气表设备";
            result.IsrName = false;
            result.IsRegisterMpush = false;
            result.IsSet = false;

        } else if ("2".equals(type)) {
            result = new GasMine(baseactivity);
            result.title = "气表详情";
            result.IsrName = true;
            result.IsSet = true;
        } else {
            result = new GasNotice(baseactivity);
            result.title = "气表通知";
            result.IsrName = false;
        }
        return result;
    }

    public static ConcentratorBase getConcentratorBaseInstance(String type, Baseactivity baseactivity) {
        ConcentratorBase result;
        if ("1".equals(type)) {
            result = new ConcentratorScence(baseactivity);
            result.title = "场景环境检测仪";
            result.IsrName = false;
            result.IsRegisterMpush = false;

        } else if ("2".equals(type)) {
            result = new ConcentratorMine(baseactivity);
            result.title = "环境检测仪详情";
            result.IsrName = true;
        } else {
            result = new ConcentratorNotice(baseactivity);
            result.title = "环境检测仪通知";
            result.IsrName = false;
        }
        return result;
    }

    public static ElectricBase getElectricBaseInstance(String type, Baseactivity baseactivity) {
        ElectricBase result;
        if ("1".equals(type)) {
            result = new EletricScence(baseactivity);
            result.title = "场景单相电";
            result.IsrName = false;
            result.IsRegisterMpush = false;

        } else if ("2".equals(type)) {
            result = new EletricMine(baseactivity);
            result.title = "单相电详情";
            result.IsrName = true;
            result.IsRegisterMpush = true;
        } else {
            result = new EletricNotice(baseactivity);
            result.title = "单相电通知";
            result.IsrName = false;
            result.IsRegisterMpush = true;
        }
        return result;
    }

    public static SmartSwitchBase getSmartSwitchBaseInstance(String type, Baseactivity baseactivity) {
        SmartSwitchBase result;
        if ("1".equals(type)) {
            result = new SmartSwitchScence(baseactivity);
            result.title = "场景智能开关";
            result.IsrName = false;
            result.IsRegisterMpush = false;

        } else if ("2".equals(type)) {
            result = new SmartSwitchMine(baseactivity);
            result.title = "智能开关详情";
            result.IsrName = true;
            result.IsSet = true;
        } else {
            result = new SmartSwitchNotice(baseactivity);
            result.title = "智能开关通知";
            result.IsrName = false;
        }
        return result;
    }

    public static WaterBase getWaterBaseInstance(String type, Baseactivity baseactivity) {
        WaterBase result;
        if ("1".equals(type)) {
            result = new WaterScence(baseactivity);
            result.title = "场景水表设备";
            result.IsrName = false;
            result.IsRegisterMpush = false;

        } else if ("2".equals(type)) {
            result = new WaterMine(baseactivity);
            result.title = "水表详情";
            result.IsrName = true;
            result.IsSet = true;
        } else {
            result = new WaterNotice(baseactivity);
            result.title = "水表通知";
            result.IsrName = false;
        }
        return result;
    }

    public static SmartPlugBase getSmartPlugBaseInstance(String type, Baseactivity baseactivity) {
        SmartPlugBase result;
        if ("1".equals(type)) {
            result = new SmartPlugScence(baseactivity);
            result.title = "场景智能插座";
            result.IsrName = false;
            result.IsRegisterMpush = false;

        } else if ("2".equals(type)) {
            result = new SmartPlugMine(baseactivity);
            result.title = "智能插座详情";
            result.IsrName = true;
            result.IsSet = true;
        } else {
            result = new SmartPlugNotice(baseactivity);
            result.title = "智能插座通知";
            result.IsrName = false;
        }
        return result;
    }


    public static GlassShockBase getGlassShockBaseInstance(String type, Baseactivity baseactivity) {
        GlassShockBase result;
        if ("1".equals(type)) {
            result = new GlassShockScence(baseactivity);
            result.title = "场景玻璃震荡";
            result.IsrName = false;
            result.IsRegisterMpush = false;

        } else if ("2".equals(type)) {
            result = new GlassShockMine(baseactivity);
            result.title = "玻璃震荡详情";
            result.IsrName = true;
            result.IsSet = true;
        } else {
            result = new GlassShockNotice(baseactivity);
            result.title = "玻璃震荡通知";
            result.IsrName = false;
        }
        return result;
    }

    public static DoorAlarmBase getDoorAlarmBaseInstance(String type, Baseactivity baseactivity) {
        DoorAlarmBase result;
        if ("1".equals(type)) {
            result = new DoorAlarmScence(baseactivity);
            result.title = "场景门磁报警";
            result.IsrName = false;
            result.IsRegisterMpush = false;

        } else if ("2".equals(type)) {
            result = new DoorAlarmMine(baseactivity);
            result.title = "门磁报警详情";
            result.IsrName = true;
            result.IsSet = true;
        } else {
            result = new DoorAlarmNotice(baseactivity);
            result.title = "门磁报警通知";
            result.IsrName = false;
        }
        return result;
    }


    public static CombustibleBase getCombustibleBaseInstance(String type, Baseactivity baseactivity) {
        CombustibleBase result;
        if ("1".equals(type)) {
            result = new CombustibleScence(baseactivity);
            result.title = "可燃气体报警";
            result.IsrName = false;
            result.IsRegisterMpush = false;

        } else if ("2".equals(type)) {
            result = new CombustibleMine(baseactivity);
            result.title = "可燃气体详情";
            result.IsrName = true;
            result.IsSet = true;
        } else {
            result = new CombustibleNotice(baseactivity);
            result.title = "可燃气体通知";
            result.IsrName = false;
        }
        return result;
    }


    public static CurtainBase getCurtainBaseInstance(String type, Baseactivity baseactivity) {
        CurtainBase result;
        if ("1".equals(type)) {
            result = new CurtainScence(baseactivity);
            result.title = "窗帘报警";
            result.IsrName = false;
            result.IsRegisterMpush = false;

        } else if ("2".equals(type)) {
            result = new CurtainMine(baseactivity);
            result.title = "窗帘详情";
            result.IsrName = true;
            result.IsSet = true;
        } else {
            result = new CurtainNotice(baseactivity);
            result.title = "窗帘通知";
            result.IsrName = false;
        }
        return result;
    }


}
