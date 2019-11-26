package com.bchilakalapudi.filemanager.ui.gallery;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import com.bchilakalapudi.filemanager.ListAdapter;
import com.bchilakalapudi.filemanager.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private AdView mAdView;

    ArrayList<String> m_item;
    ArrayList<String> m_path;
    ArrayList<String> m_files;
    ArrayList<String> m_filesPath;
    String m_curDir;
    ListAdapter m_listAdapter;
    String m_text;


    private String m_root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_extsdcard, container, false);
       /* final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        String ext="storage/";
        File me_file = new File(ext);

        String extname="";
        for(File fi:me_file.listFiles()){
            if(!fi.getName().equals("self") && !fi.getName().equals("emulated")) {
                extname = "/storage/"+fi.getName();
                break;
            }
        }

        m_root =extname;
                //Environment.getExternalStorageDirectory().getPath();
        ListView lvobj=(ListView)root.findViewById(R.id.ext_rl_lvListRoot);



        mAdView = root.findViewById(R.id.int_adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("A03E8CB1F034A0790951F2713CE1E0CE").build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        getDirFromRoot(m_root,lvobj);

        return root;
    }
    //get directories and files from selected path
    public void getDirFromRoot(String p_rootPath, final ListView lvobj) {
        Log.d("p_rootPaths", "" + p_rootPath);
        Boolean m_isRoot = true;
        m_item = new ArrayList<String>();
        m_path = new ArrayList<String>();
        m_files = new ArrayList<String>();
        m_filesPath = new ArrayList<String>();

        File m_file=new File(p_rootPath);
        File[] m_filesArray = m_file.listFiles();

        if (m_filesArray != null)
            Log.d("m_filesArray:", "" + m_filesArray.length);

        if (!p_rootPath.equals(m_root)) {
            m_item.add("../");
            m_path.add(m_file.getParent());
            m_isRoot = true;
        }
        m_curDir = p_rootPath;

        //sorting file list in alphabetical order
        if (m_filesArray != null) {
            Arrays.sort(m_filesArray);
            for (int i = 0; i < m_filesArray.length; i++) {
                File file = m_filesArray[i];
                if (!file.getName().startsWith(".")) {
                    if (file.isDirectory()) {
                        m_item.add(file.getName());
                        m_path.add(file.getPath());
                    } else {
                        m_files.add(file.getName());
                        m_filesPath.add(file.getPath());
                    }
                }
            }
            for (String m_AddFile : m_files) {
                m_item.add(m_AddFile);
            }
            for (String m_AddPath : m_filesPath) {
                m_path.add(m_AddPath);
            }
        }

        Log.d("m_item", "" + m_item);
        Log.d("m_path", "" + m_path);

        // Boolean m_isRoot = true;
        ListView m_RootList = lvobj;
        //(ListView) findViewById(R.id.rl_lvListRoot);
        m_listAdapter = new ListAdapter(getContext(), m_item, m_path, m_isRoot);
        m_RootList.setAdapter(m_listAdapter);
        m_RootList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                File m_isFile = new File(m_path.get(position));
                if (m_isFile.isDirectory()) {
                    getDirFromRoot(m_isFile.toString(),lvobj);
                } else {

                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Uri uri = FileProvider.getUriForFile(
                                getContext(),  "com.bchilakalapudi.filemanager.fileprovider",m_isFile);

                        String url= m_isFile.getName();
                        //.substring(m_isFile.getName().lastIndexOf("."));
                        Log.d("fileext",""+url);

                        // grantUriPermission(getContext().getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);


                        // Check what kind of file you are trying to open, by comparing the url with extensions.
                        // When the if condition is matched, plugin sets the correct intent (mime) type,
                        // so Android knew what application to use to open the file
                        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                            // Word document
                            intent.setDataAndType(uri, "application/msword");
                        } else if(url.toString().contains(".pdf")) {
                            // PDF file
                            intent.setDataAndType(uri, "application/pdf");
                        } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                            // Powerpoint file
                            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                        } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                            // Excel file
                            intent.setDataAndType(uri, "application/vnd.ms-excel");
                        } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
                            // WAV audio file
                            intent.setDataAndType(uri, "application/x-wav");
                        } else if(url.toString().contains(".rtf")) {
                            // RTF file
                            intent.setDataAndType(uri, "application/rtf");
                        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                            // WAV audio file
                            intent.setDataAndType(uri, "audio/x-wav");
                        } else if(url.toString().contains(".gif")) {
                            // GIF file
                            intent.setDataAndType(uri, "image/gif");
                        } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                            // JPG file
                            intent.setDataAndType(uri, "image/jpeg");
                        } else if(url.toString().contains(".txt")) {
                            // Text file
                            intent.setDataAndType(uri, "text/plain");
                        } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                            // Video files
                            intent.setDataAndType(uri, "video/*");
                        } else {
                            //if you want you can also define the intent type for any other file

                            //additionally use else clause below, to manage other unknown extensions
                            //in this case, Android will show all applications installed on the device
                            //so you can choose which application to use
                            intent.setDataAndType(uri, "*/*");
                        }



                        //   intent.setDataAndType(uri,"video/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // no Activity to handle this kind of files
                        Log.d("exception",""+e);
                    }

                }
            }
        });
    }
}