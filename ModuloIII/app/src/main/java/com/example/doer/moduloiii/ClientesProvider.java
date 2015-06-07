package com.example.doer.moduloiii;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.doer.moduloiii.helpers.ClientesSqliteHelper;

/**
 * Created by DOER on 05/06/2015.
 */
public class ClientesProvider  extends ContentProvider {

    // Definicion de URI del content provider

    private static final String URI = "content://com.example.doer.moduloiii/clientes/";
    public static final Uri CONTENT_URI = Uri.parse(URI);

    public static final class Clientes implements BaseColumns{
        private Clientes() {}

        public static final String COL_NOMBRE = "nombre";
        public static final String COL_TELEFONO = "telefono";
        public static final String COL_EMAIL = "email";
    }

    private ClientesSqliteHelper clientesSqliteHelper;
    private static final String BD_NOMBRE = "DBCleintes";
    private static final int BD_VERSION = 2;
    private static final String TABLA_CLIENTES = "Clientes";

    private static final int CLIENTES = 1;
    private static final int CLIENTES_ID = 2;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.doer.moduloiii", "clientes", CLIENTES);
        uriMatcher.addURI("com.example.doer.moduloiii", "clientes/#", CLIENTES_ID);
    }


    @Override
    public boolean onCreate() {
        clientesSqliteHelper = new ClientesSqliteHelper(getContext(), BD_NOMBRE, null, BD_VERSION);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String where = selection;
        if(uriMatcher.match(uri) == CLIENTES_ID){
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = clientesSqliteHelper.getWritableDatabase();

        Cursor c = db.query(TABLA_CLIENTES, projection, where, selectionArgs, null, null, sortOrder);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match){
            case CLIENTES:
                return "com.android.cursor.dir/com.doer.cliente";
            case CLIENTES_ID:
                return "com.android.cursor.item/com.doer.cliente";
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long regId = 1;

        SQLiteDatabase db = clientesSqliteHelper.getWritableDatabase();

        regId = db.insert(TABLA_CLIENTES, null, values);

        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, regId);

        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cont;

        String where = selection;
        if(uriMatcher.match(uri) == CLIENTES_ID){
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = clientesSqliteHelper.getWritableDatabase();

        cont = db.delete(TABLA_CLIENTES, where, selectionArgs);

        return cont;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int cont;

        String where = selection;
        if(uriMatcher.match(uri) == CLIENTES_ID){
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = clientesSqliteHelper.getWritableDatabase();

        cont = db.update(TABLA_CLIENTES, values, where, selectionArgs);

        return cont;
    }
}
