package com.fridge;

import android.app.AlertDialog;
import com.fridge.api.FridgeLockCtrol;
import com.fridge.api.LibError;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Lock {

    private static final Logger LOGGER = Logger.getLogger( Lock.class.getName() );
    private FridgeLockCtrol fridgeLockCtrol;

    public Lock() {
        fridgeLockCtrol = new FridgeLockCtrol();
    }

    /**
     * This method return the hardware lock status
     *
     * @return return true if look, false if unlook
     */
    public boolean getLockStatus() throws Exception {
        byte[] alarmFlg = new byte[2];
        byte[] doorFlg = new byte[2];
        //byte[] passingFlg = new byte[2];
        //float[] temperature = new float[2];
        int status = fridgeLockCtrol.getStatus((byte) 1, doorFlg,alarmFlg, null, null);
        if (status != LibError.ERR_OK)
            throw new Exception("Failed to get the door's status.");
        if (doorFlg[0]==0)
        {
            LOGGER.log(Level.INFO, "The door is closed.");
            return true;
        }
        else {
            LOGGER.log(Level.INFO, "The door is open.");
            return true;
        }
    }

    /**
     * This method return the hardware alarm status
     *
     * @return return true if alarm, false if no alarm
     */
    public boolean getAlarmStatus() throws Exception {
        byte[] alarmFlg = new byte[2];
        byte[] doorFlg = new byte[2];
        //byte[] passingFlg = new byte[2];
        //float[] temperature = new float[2];
        int status = fridgeLockCtrol.getStatus((byte) 1, doorFlg,alarmFlg, null, null);
        if (status != LibError.ERR_OK)
            throw new Exception("Failed to get the door's status.");

        if (alarmFlg[0]!=0)
        {
            LOGGER.log(Level.INFO, "The door is alarming.");
            return true;
        }
        else{
            LOGGER.log(Level.INFO, "The door not is alarming.");
            return false;
        }
    }

    /**
     * This method open the lock
     *
     * @return return true if the look was opened, false otherwise
     */
    public boolean open()
    {
        int response = fridgeLockCtrol.openDoor(1);
        if (response == LibError.ERR_OK)
        {
            LOGGER.log(Level.INFO, "Open the door successfully.");
            return true;
        }
        else
        {
            LOGGER.log(Level.INFO, "Failed to open the door.");
            return false;
        }
    }



}
