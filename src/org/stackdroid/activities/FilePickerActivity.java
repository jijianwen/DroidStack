package org.stackdroid.activities;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.stackdroid.R;

public class FilePickerActivity extends ListActivity {

    private List<String> item = null;
    private List<String> path = null;
    private String root = Environment.getExternalStorageDirectory( ).getAbsolutePath();
    private TextView myPath;

    /**
     *
     *
     *
     *
     *   
    class customAdapter extends ArrayAdapter<File> {
	@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int type = getItemViewType(position);
            System.out.println("getView " + position + " " + convertView + " type = " + type);
            if (convertView == null) {
                holder = new ViewHolder();
                switch (type) {
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.item1, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.text);
                        break;
                    case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.item2, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.textSeparator);
                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.textView.setText(mData.get(position));
            return convertView;
        }
    }
    */
    /** Called when the activity is first created. */
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filepicker);
        myPath = (TextView)findViewById(R.id.path);
        getDir(root);
    }    

    /**
     *
     *
     *
     *
     */
    private void getDir(String dirPath)
    {
	myPath.setText("Location: " + dirPath);
	item = new ArrayList<String>();
	path = new ArrayList<String>();
	File f = new File(dirPath);
	File[] files = f.listFiles();

	Arrays.sort(files);
	
	if(!dirPath.equals(root))
	    {
		item.add(root);
		path.add(root);
		item.add("../");
		path.add(f.getParent());
		
	    }

	for(int i=0; i < files.length; i++)
	    {		
		File file = files[i];	
		path.add(file.getPath());
		if(file.isDirectory())
		    item.add(file.getName() + "/");
		else
		    item.add(file.getName());		
	    }
	
	ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.filepicker_row, item);
	setListAdapter(fileList);
    }

    /**
     *
     *
     *
     *
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	File file = new File(path.get(position));
	
	if (file.isDirectory())
	    {
		if(file.canRead())
		    getDir(path.get(position));
		else {
		    new AlertDialog.Builder(this)
			.setIcon(R.drawable.icon)
			.setTitle("[" + file.getName() + "] folder can't be read!")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			    }).show();   
		}
	    }
	else {		
		new AlertDialog.Builder(this)
		    .setIcon(R.drawable.icon)		    
		    .setTitle("[" + file.getName() + "]")
		    .setPositiveButton("OK", 				       
				       new DialogInterface.OnClickListener() {
					   @Override
					   public void onClick(DialogInterface dialog, int which) {
					       // TODO Auto-generated method stub
					   }
					   
				       }).show();	
	    }	
    }
    
}