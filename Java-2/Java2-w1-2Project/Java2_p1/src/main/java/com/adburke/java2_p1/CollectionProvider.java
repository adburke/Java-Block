/*
 * Project:		Java2_p1
 *
 * Package:		Java2_p1
 *
 * Author:		Aaron Burke
 *
 * Date:		12/3/2013
 */

package com.adburke.java2_p1;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.provider.BaseColumns;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CollectionProvider extends ContentProvider{

    public static final String AUTHORITY = "com.adburke.java2_p1.CollectionProvider";

    public static class JsonData implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/items");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.adburke.java2_p1.item";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.adburke.java2_p1.item";

        public static final String NAME_COLUMN = "productName";
        public static final String VENDOR_COLUMN = "vendor";
        public static final String PRICE_COLUMN = "productPrice";

        public static final String[] PROJECTION = {"_Id", NAME_COLUMN, VENDOR_COLUMN, PRICE_COLUMN};

        private JsonData() {};
    }

    public static final int ITEMS = 1;
    public static final int ITEMS_ID = 2;
    public static final int ITEMS_TYPE = 3;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, "items/", ITEMS);
        uriMatcher.addURI(AUTHORITY, "items/#", ITEMS_ID);
        uriMatcher.addURI(AUTHORITY, "items/type/", ITEMS_TYPE);
    }


    @Override
    public boolean onCreate() {
        return false;
    }

    /**
     * Implement this to handle query requests from clients.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p/>
     *
     * @param uri           The URI to query. This will be the full URI sent by the client;
     *                      if the client is requesting a specific record, the URI will end in a record number
     *                      that the implementation should parse and add to a WHERE or HAVING clause, specifying
     *                      that _id value.
     * @param projection    The list of columns to put into the cursor. If
     *                      {@code null} all columns are included.
     * @param selection     A selection criteria to apply when filtering rows.
     *                      If {@code null} then all rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @param sortOrder     How the rows in the cursor should be sorted.
     *                      If {@code null} then the provider is free to define the sort order.
     * @return a Cursor or {@code null}.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        MatrixCursor result = new MatrixCursor(JsonData.PROJECTION);

        String JSONString = FileManager.readFile(getContext(), Api_browser.mJsonFile);
        JSONObject query = null;
        JSONArray resultsArray = null;
        JSONArray siteDetails = null;
        JSONArray latestOffers = null;

        try {
            query = new JSONObject(JSONString);
            resultsArray = query.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (resultsArray == null) {
            return result;
        }

        switch (uriMatcher.match(uri)) {
            case ITEMS:

                for (int i = 0, j = resultsArray.length(); i < j; i++) {
                    try {
                        siteDetails = resultsArray.getJSONObject(i).getJSONArray("sitedetails");
                        latestOffers = siteDetails.getJSONObject(0).getJSONArray("latestoffers");

                        result.addRow(new Object[] {i+1,resultsArray.getJSONObject(i).get("name"),
                            latestOffers.getJSONObject(0).get("seller"), latestOffers.getJSONObject(0).get("price") });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case ITEMS_ID:

                // Get the last segment value from the uri in this case the string number
                String itemId = uri.getLastPathSegment();

                // Create an integer out of the string and check for correct int format
                int indexVal;
                try {
                    indexVal = Integer.parseInt(itemId);
                } catch (NumberFormatException e) {
                    Log.e("ITEMS_ID URI", "Incorrect format" );
                    break;
                }

                // Check if the item id is with valid range of results
                if (indexVal <= 0 || indexVal > resultsArray.length()) {
                    Log.e("ITEMS_ID URI", "ID number not within valid range");
                    break;
                }

                try {

                    siteDetails = resultsArray.getJSONObject(indexVal - 1).getJSONArray("sitedetails");
                    latestOffers = siteDetails.getJSONObject(0).getJSONArray("latestoffers");

                    result.addRow(new Object[] {indexVal,resultsArray.getJSONObject(indexVal - 1).get("name"),
                            latestOffers.getJSONObject(0).get("seller"), latestOffers.getJSONObject(0).get("price") });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case ITEMS_TYPE:


        }

        Log.i("CURSOR ITEMS", result.toString());
        return result;
    }

    /**
     * Implement this to handle requests for the MIME type of the data at the
     * given URI.  The returned MIME type should start with
     * <code>vnd.android.cursor.item</code> for a single record,
     * or <code>vnd.android.cursor.dir/</code> for multiple items.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p/>
     * <p>Note that there are no permissions needed for an application to
     * access this information; if your content provider requires read and/or
     * write permissions, or is not exported, all applications can still call
     * this method regardless of their access permissions.  This allows them
     * to retrieve the MIME type for a URI when dispatching intents.
     *
     * @param uri the URI to query.
     * @return a MIME type string, or {@code null} if there is no type.
     */
    @Override
    public String getType(Uri uri) {

//        switch (uriMatcher.match(uri)) {
//            case ITEMS:
//                return JsonData.CONTENT_TYPE;
//            case ITEMS_ID:
//                return JsonData.CONTENT_ITEM_TYPE;
//            case ITEMS_TYPE:
//                return JsonData.CONTENT_ITEM_TYPE;
//        }

        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
