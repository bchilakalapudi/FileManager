package com.bchilakalapudi.filemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {
    private List<String> m_item;
    private List<String> m_path;
    public ArrayList<Integer> m_selectedItem;
    Context m_context;
    Boolean m_isRoot;
    public ListAdapter(Context p_context,List<String> p_item, List<String> p_path,Boolean p_isRoot) {
        m_context=p_context;
        m_item=p_item;
        m_path=p_path;
        m_selectedItem=new ArrayList<Integer>();
        m_isRoot=p_isRoot;
    }


    @Override
    public int getCount() {
        int res=0;
        if(m_item!=null)
            res=m_item.size();
        return res;
    }

    @Override
    public Object getItem(int position) {
        return m_item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int p_position, View p_convertView, ViewGroup p_parent)
    {
        View m_view = null;
        ViewHolder m_viewHolder = null;
        if (p_convertView == null)
        {
            LayoutInflater m_inflater = LayoutInflater.from(m_context);
            m_view = m_inflater.inflate(R.layout.list_row, null);
            m_viewHolder = new ViewHolder();
            m_viewHolder.m_tvFileName = (TextView) m_view.findViewById(R.id.lr_tvFileName);
            m_viewHolder.m_tvDate = (TextView) m_view.findViewById(R.id.lr_tvdate);
            m_viewHolder.m_ivIcon = (ImageView) m_view.findViewById(R.id.lr_ivFileIcon);
           // m_viewHolder.m_cbCheck = (CheckBox) m_view.findViewById(R.id.lr_cbCheck);
            m_viewHolder.m_tvFileSize= (TextView) m_view.findViewById(R.id.lr_tvFileSize);
            m_view.setTag(m_viewHolder);
        }
        else
        {
            m_view = p_convertView;
            m_viewHolder = ((ViewHolder) m_view.getTag());
        }
        if(!m_isRoot && p_position == 0) {
            m_viewHolder.m_cbCheck.setVisibility(View.INVISIBLE);

        }
       // if(p_position>0){
            m_viewHolder.m_tvFileName.setText(m_item.get(p_position));
            m_viewHolder.m_ivIcon.setImageResource(setFileImageType(new File(m_path.get(p_position))));
            m_viewHolder.m_tvDate.setText(getLastDate(p_position));
            m_viewHolder.m_tvFileSize.setText(getFileSize(p_position));

          /*  m_viewHolder.m_cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        m_selectedItem.add(p_position);
                    } else {
                        m_selectedItem.remove(m_selectedItem.indexOf(p_position));
                    }
                }
            });*/
     //   }
        return m_view;
    }

    class ViewHolder
    {
        CheckBox m_cbCheck;
        ImageView m_ivIcon;
        TextView m_tvFileName;
        TextView m_tvDate;
        TextView m_tvFileSize;
    }

    private int setFileImageType(File m_file)
    {
        int m_lastIndex=m_file.getAbsolutePath().lastIndexOf(".");
        String m_filepath=m_file.getAbsolutePath();
        if (m_file.isDirectory())
            return R.drawable.ic_folder_black_50dp;
        else
        {
           if(m_filepath!=null && m_filepath.contains(".")) {
               if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".png")) {
                   return R.drawable.ic_insert_drive_file_black_50dp;
               } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".jpg")) {
                   return R.drawable.ic_insert_drive_file_black_50dp;
               }
           }else {
               return R.drawable.ic_insert_drive_file_black_50dp;
           }
        }
        return R.drawable.ic_insert_drive_file_black_50dp;
    }

    String getLastDate(int p_pos)
    {
        File m_file=new File(m_path.get(p_pos));
        SimpleDateFormat m_dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return m_dateFormat.format(m_file.lastModified());
    }
    String getFileSize(int p_pos){
        File m_file=new File(m_path.get(p_pos));
        String filesize="";
        filesize=getFileSize(m_file);
        return filesize;
    }
    public static String getFileSize(File file) {
        String modifiedFileSize = null;
        double fileSize = 0.0;
        if (file.isFile()) {
            fileSize = (double) file.length();//in Bytes

            if (fileSize < 1024) {
                modifiedFileSize = String.valueOf(fileSize).concat("B");
            } else if (fileSize > 1024 && fileSize < (1024 * 1024)) {
                modifiedFileSize = String.valueOf(Math.round((fileSize / 1024 * 100.0)) / 100.0).concat("KB");
            } else {
                modifiedFileSize = String.valueOf(Math.round((fileSize / (1024 * 1204) * 100.0)) / 100.0).concat("MB");
            }
        } else {

             modifiedFileSize = getFileSizeFromBytes(dirSize(file));
        }

        return modifiedFileSize;
    }
    /**
     * Return the size of a directory in bytes
     */

    private static long dirSize(File dir) {

        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            if(fileList!=null) {
                for (int i = 0; i < fileList.length; i++) {
                    // Recursive call if it's a directory
                    if (fileList[i].isDirectory()) {
                        result += dirSize(fileList[i]);
                    } else {
                        // Sum the file size in bytes
                        result += fileList[i].length();
                    }
                }
            }
            return result; // return the file size
        }
        return 0;
    }

    public static String getFileSizeFromBytes(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}