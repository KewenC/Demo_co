package com.kewenc.demo_co;

import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
//    PackageManager packageManager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.btn);
//        packageManager = getPackageManager();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                clearCache();
                Toast.makeText(MainActivity.this,"点击",Toast.LENGTH_SHORT).show();
//                getData();//获取缓存数据等
                McleanCache();//清除缓存
            }
        });
    }
//    private void clearCache(){
//        try {
//            //Log.d("TAGF","if="+rublishGroupModelList.get(i).checked);//TODO:部分机型能清除缓存，部分不行。反射方法异常-2017年10月16日
//            if (true) {
//                Method localMethod = packageManager.getClass().getMethod("freeStorageAndNotify", Long.TYPE,
//                        IPackageDataObserver.class);
//                Long localLong = Long.valueOf(getEnvironmentSize() - 1L);
//                Object[] arrayOfObject = new Object[2];
//                arrayOfObject[0] = localLong;
//                localMethod.invoke(packageManager, localLong, new IPackageDataObserver.Stub() {
//
//                    @Override
//                    public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
//                    }
//                });
//            }
//        } catch (Exception e) {
//            Log.d("TAGF",e.toString());
//            e.printStackTrace();
//        }
//    }
    private void getData(){
        Log.d("TAG","点击");
        try {
            PackageManager pm = getPackageManager();
            Class clazz = Class.forName("android.content.pm.PackageManager");
            Method method = clazz.getMethod("getPackageSizeInfo",new Class[]{String.class
            ,IPackageStatsObserver.class});
            method.invoke(pm,new Object[]{"com.douban.frodo",mStatsObserver});
//            Method[] methods = clazz.getMethods();
//            for (Method method:methods){
//                Log.d("TAG",method.getName());
//                Class[] clss = method.getParameterTypes();
//                for (Class c:clss){
//                    Log.d("TAG",c.getName());
//                }
//                Log.d("TAG","**********************************");
//            }
        }catch (Exception e){
            Log.d("TAG",e.toString());
            e.printStackTrace();
        }

    }
    final IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub(){

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            long cacheSize = pStats.cacheSize;
            long codeSize = pStats.codeSize;
            long dataSize = pStats.dataSize;
            Log.d("TAG","缓存cacheSize="+cacheSize+"应用codeSize="+codeSize+"数据dataSize="+dataSize);//get_package_size权限
        }
    };
    private static long getEnvironmentSize() {
        File localFile = Environment.getDataDirectory();
        long l1;
        if (localFile == null)
            l1 = 0L;
        while (true) {
            String str = localFile.getPath();
            StatFs localStatFs = new StatFs(str);
            long l2 = localStatFs.getBlockSize();
            l1 = localStatFs.getBlockCount() * l2;
            return l1;
        }
    }

    private void McleanCache(){
        Log.d("TAG","点击");
        try {
            PackageManager pm = getPackageManager();
            Class clazz = Class.forName("android.content.pm.PackageManager");
//            Method method = clazz.getMethod("getPackageSizeInfo",new Class[]{String.class
//                    ,IPackageStatsObserver.class});
//            method.invoke(pm,new Object[]{"com.douban.frodo",mStatsObserver});
//            Method[] methods = clazz.getMethods();
//            for (Method method:methods){
//                Log.d("TAG",method.getName());
//                Class[] clss = method.getParameterTypes();
//                for (Class c:clss){
//                    Log.d("TAG",c.getName());
//                }
//                Log.d("TAG","**********************************");
//            }
            Method method = clazz.getMethod("deleteApplicationCacheFiles",new Class[]{String.class
            , IPackageDataObserver.class});
            ClearCacheObserver clearCacheObserver = new ClearCacheObserver();
            method.invoke(pm,new Object[]{"com.douban.frodo",clearCacheObserver});
            //delete_cache_files权限
        }catch (Exception e){
            Log.d("TAG",e.toString());
            e.printStackTrace();
        }

    }
    final class ClearCacheObserver extends IPackageDataObserver.Stub{

        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

        }
    }
}
