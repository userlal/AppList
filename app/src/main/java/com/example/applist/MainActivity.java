package com.example.applist;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppListAdapter adapter;
    private List<ApplicationInfo> appList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        appList = getInstalledApps();

        adapter = new AppListAdapter(this, appList);
        recyclerView.setAdapter(adapter);
    }

    private List<ApplicationInfo> getInstalledApps() {
        PackageManager packageManager = getPackageManager();
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
    }

    private class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

        private Context context;
        private List<ApplicationInfo> appList;

        public AppListAdapter(Context context, List<ApplicationInfo> appList) {
            this.context = context;
            this.appList = appList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final ApplicationInfo app = appList.get(position);
            String appName = app.loadLabel(getPackageManager()).toString();
            holder.textView.setText(appName);
            if (isAnidescPackage(app.packageName)) {
                holder.textView.setTextColor(Color.RED);
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Программа анидеск обнаружена!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return appList.size();
        }

        private boolean isAnidescPackage(String packageName) {
            // Здесь вы можете добавить пакетные имена других известных программ анидеск
            return packageName.equals("com.package.anidesc1") || packageName.equals("com.package.anidesc2");
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}