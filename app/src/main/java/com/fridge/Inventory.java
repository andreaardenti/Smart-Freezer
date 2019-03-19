package com.fridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rfid.api.ADReaderInterface;
import com.rfid.api.ISO15693Interface;
import com.rfid.api.ISO15693Tag;
import com.rfid.def.ApiErrDefinition;
import com.rfid.def.RfidDef;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Inventory{

    private static final Logger LOGGER = Logger.getLogger( Inventory.class.getName() );
    private boolean bThreadRunFlg = false;
    private ADReaderInterface m_reader = new ADReaderInterface();


    /* public JSONObject vectorISO15693TagToJsonObject(Vector<ISO15693Tag> tagVector){
        JSONObject json=new JSONObject();
        JSONArray jsonArray=new JSONArray(tagVector);
        try {
            json.put("purchase",jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("JSON OBJ: " + json);
        return json;
    }*/


    public Vector<ISO15693Tag> readTagsList(ADReaderInterface m_reader){


        byte newAI = RfidDef.AI_TYPE_NEW;
        byte useAnt[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        Object hInvenParamSpecList = ADReaderInterface.RDR_CreateInvenParamSpecList();
        ISO15693Interface.ISO15693_CreateInvenParam(hInvenParamSpecList, (byte) 0, false, (byte) 0x00, (byte) 0);

            Vector<ISO15693Tag> tagsList = new Vector<ISO15693Tag>();
            long t1 = System.currentTimeMillis();
            int iret = m_reader.RDR_TagInventory(newAI, useAnt, 0,
                    hInvenParamSpecList);
            if(iret==ApiErrDefinition.NO_ERROR)
            {
                //Close RF if inventory is successful.
                m_reader.RDR_CloseRFTransmitter();
            }
            long t2 = System.currentTimeMillis();
            if (iret == ApiErrDefinition.NO_ERROR)
            {
                Object tagReport = m_reader.RDR_GetTagDataReport(RfidDef.RFID_SEEK_FIRST);
                while (tagReport != null)
                {
                    ISO15693Tag tag = new ISO15693Tag();
                    iret = ISO15693Interface.ISO15693_ParseTagDataReport(tagReport, tag);
                    if (iret == ApiErrDefinition.NO_ERROR)
                    {
                        LOGGER.log(Level.INFO, "Added TAG to tag list: "+tag);
                        tagsList.add(tag);
                    }
                    tagReport = m_reader.RDR_GetTagDataReport(RfidDef.RFID_SEEK_NEXT);
                }
            }
            else
            {
                // mFailCount++;
            }
            LOGGER.log(Level.INFO, "Return TagList: "+tagsList);
            return tagsList;

    }

    private class InventoryThread extends Thread {

    public void run()
    {
        int mLoopCount = 0;
        // int mFailCount = 0;
        bThreadRunFlg = true;
        byte newAI = RfidDef.AI_TYPE_NEW;
        byte useAnt[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        Object hInvenParamSpecList = ADReaderInterface
                .RDR_CreateInvenParamSpecList();
        ISO15693Interface.ISO15693_CreateInvenParam(hInvenParamSpecList,
                (byte) 0, false, (byte) 0x00, (byte) 0);
        while (bThreadRunFlg)
        {
            Vector<ISO15693Tag> tagsList = new Vector<ISO15693Tag>();
            long t1 = System.currentTimeMillis();
            int iret = m_reader.RDR_TagInventory(newAI, useAnt, 0,
                    hInvenParamSpecList);
            if(iret==ApiErrDefinition.NO_ERROR)
            {
                //Close RF if inventory is successful.
                m_reader.RDR_CloseRFTransmitter();
            }
            long t2 = System.currentTimeMillis();
            mLoopCount++;
            if (iret == ApiErrDefinition.NO_ERROR)
            {
                Object tagReport = m_reader
                        .RDR_GetTagDataReport(RfidDef.RFID_SEEK_FIRST);
                while (tagReport != null)
                {
                    ISO15693Tag tag = new ISO15693Tag();
                    iret = ISO15693Interface.ISO15693_ParseTagDataReport(
                            tagReport, tag);
                    if (iret == ApiErrDefinition.NO_ERROR)
                    {
                        tagsList.add(tag);
                    }
                    tagReport = m_reader
                            .RDR_GetTagDataReport(RfidDef.RFID_SEEK_NEXT);
                }
            }
            else
            {
                // mFailCount++;
            }
            //Message msg = mHandler.obtainMessage();
            //msg.what = MSG_UPDATE_TAGLIST;
            //msg.obj = tagsList;
            //msg.arg1 = mLoopCount;
            //msg.arg2 = (int) (t2 - t1);
            //mHandler.sendMessage(msg);
        }
        //Message msg = mHandler.obtainMessage();
        //msg.what = MSG_THREAD_END;
        //mHandler.sendMessage(msg);
        bThreadRunFlg = false;
    }


}


/**Nested static class InventoryAdapter
 *
 */
private static class InventoryAdapter extends BaseAdapter
{
    private List<InventoryReport> list;
    private LayoutInflater inflater;

    public InventoryAdapter(Context context, List<InventoryReport> list)
    {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public int getCount()
    {
        return list.size();
    }

    public Object getItem(int position)
    {
        return list.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        InventoryReport inventoryReport = (InventoryReport) this
                .getItem(position);
        ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            //convertView = inflate.inflate(R.xml.inventorylist_tittle, null);
            //viewHolder.mTextUid = (TextView) convertView.findViewById(R.id.tv_inventoryUid);
            //viewHolder.mTextAntenna = (TextView) convertView.findViewById(R.id.tv_inventoryAntenna);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTextUid.setText(inventoryReport.getUidStr());
        viewHolder.mTextAntenna.setText(inventoryReport.getAntenna() + "");
        return convertView;
    }

    public class ViewHolder
    {
        public TextView mTextUid;
        public TextView mTextAntenna;
    }
}

/**Nested static class InventoryReport
 *
 */
private static class InventoryReport
{
    private String uidStr;
    private int antenna;

    public InventoryReport(String uid, int ant)
    {
        super();
        this.setUidStr(uid);
        this.setAntenna(ant);
    }

    public String getUidStr()
    {
        return uidStr;
    }

    public void setUidStr(String uidStr)
    {
        this.uidStr = uidStr;
    }

    public int getAntenna()
    {
        return antenna;
    }

    public void setAntenna(int antenna)
    {
        this.antenna = antenna;
    }

}

}



