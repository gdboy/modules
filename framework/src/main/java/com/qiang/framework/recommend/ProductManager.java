package com.qiang.framework.recommend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qiang.framework.helpers.FileHelper;
import com.qiang.framework.helpers.PlayerPrefs;
import com.qiang.framework.helpers.SystemHelper;
import com.qiang.framework.helpers.LogHelper;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Lin on 2016/7/11.
 */
public class ProductManager
{
    private static Product[] products;

    private static Gson gson = new GsonBuilder().create();

    static
    {
        String json = PlayerPrefs.getString("product.json");
        if(!json.equals("") && PlayerPrefs.getLong("lastUpdateTime") == SystemHelper.getLastUpdateTime())
        {
            LogHelper.info("读取持久化产品数据");

            try
            {
                products = gson.fromJson(json, Product[].class);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if(products == null)
        {
            LogHelper.info("读取内置产品数据");

            PlayerPrefs.setLong("lastUpdateTime", SystemHelper.getLastUpdateTime());

            json = FileHelper.readAssetFileToString("product.json");
            products = gson.fromJson(json, Product[].class);
        }

        for(int i=0;i<products.length;i++)
        {
            Product product = products[i];
            if(product.app_scanimg != null && !product.app_scanimg.isEmpty())
                product.screenshotUrls = product.app_scanimg.split(",");
        }
    }

    public static Product[] getProducts()
    {
        final Product product = ProductManager.getProduct(SystemHelper.getPackageName());

        Arrays.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product product1, Product product2) {
                if(Math.abs(product1.recommend - product2.recommend) >= 3)
                    return product2.recommend - product1.recommend;

                if(product != null)
                {
                    if(product1.multiplayer != product2.multiplayer)
                    {
                        if(product1.multiplayer == product.multiplayer)
                            return -1;
                        else
                            return 1;
                    }

                    if(product1.app_type != null && !product1.app_type.equals(product2.app_type))
                    {
                        if(product1.app_type.equals(product.app_type))
                            return -1;
                        else
                            return 1;
                    }
                }

                if(product1.versionCode != product2.versionCode)
                    return product2.versionCode - product1.versionCode;

                return product2.dangbei_appId - product1.dangbei_appId;
            }
        });

        return products;
    }

    public static Product getProduct(String packageName)
    {
        for (Product product : products)
        {
            if(product.packageName.equals(packageName))
                return product;
        }

        return null;
    }

    public static void save()
    {
        String json = gson.toJson(products);
        PlayerPrefs.setString("product.json", json);
    }
}
