/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.opensdk.data;

public class DBTable {

    public class TABLE_OPEN_VERSON {
        public static final String TABLE_OPEN_VERSON_NAME = "open_version";
        public static final String COLUMN_name = "name";
        public static final String COLUMN_version = "version";
        public static final String error_code_version = "error_code_version";
    }

    public class TABLE_ERROR_CODE {
        public static final String TABLE_ERROR_CODE_NAME = "open_error_code";
        public static final String COLUMN_module_code = "module_code";
        public static final String COLUMN_detail_code = "detail_code";
        public static final String COLUMN_description = "description";
        public static final String COLUMN_solution = "solution";
        public static final String COLUMN_update_time = "update_time";
    }
}

