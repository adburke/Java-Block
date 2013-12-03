/*
 * Project:		Java2_p1
 *
 * Package:		Java2_p1
 *
 * Author:		Aaron Burke
 *
 * Date:		11/26/2013
 */

package com.adburke.java2_p1;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.semantics3.api.Products;

import org.json.JSONObject;

import java.io.IOException;


import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;


public class Api_browser extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_browser);

        Products products = new Products(
                "SEM3F98291C7F783DF843B12AA522D7D544C",
                "ZGY1MTRmMDlhYTU1NWE5ZjYyNDVlNzcwMTMxOTY1MGU"
        );

        /* Build the query */
        products
                .productsField( "cat_id", 4992 )
                .productsField( "brand", "Toshiba" );

/* Make the query */
        JSONObject results = null;
        try {
            results = products.getProducts();
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
/* or */
        try {
            results = products.get();
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

/* View the results of the query */
        Log.i("Results: ", results.toString());

    }



}
