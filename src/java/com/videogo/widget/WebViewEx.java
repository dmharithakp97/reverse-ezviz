/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.os.Build$VERSION
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.webkit.JsPromptResult
 *  android.webkit.WebChromeClient
 *  android.webkit.WebView
 *  org.json.JSONArray
 *  org.json.JSONObject
 */
package com.videogo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.videogo.widget.CompatWebViewClient;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebViewEx
extends WebView {
    private static final boolean DEBUG = true;
    private static final String VAR_ARG_PREFIX = "arg";
    private static final String MSG_PROMPT_HEADER = "MyApp:";
    private static final String KEY_INTERFACE_NAME = "obj";
    private static final String KEY_FUNCTION_NAME = "func";
    private static final String KEY_ARG_ARRAY = "args";
    private static final String[] mFilterMethods = new String[]{"getClass", "hashCode", "notify", "notifyAll", "equals", "toString", "wait"};
    private HashMap<String, Object> mJsInterfaceMap = new HashMap();
    private String mJsStringCache = null;

    public WebViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(context);
    }

    public WebViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public WebViewEx(Context context) {
        super(context);
        this.init(context);
    }

    private void init(Context context) {
        this.setWebChromeClient(new WebChromeClientEx(this));
        this.setWebViewClient(new WebViewClientEx(this));
        this.removeSearchBoxImpl();
        this.getSettings().setBuiltInZoomControls(false);
        this.getSettings().setSupportZoom(false);
        this.getSettings().setDisplayZoomControls(false);
        if (Build.VERSION.SDK_INT >= 21) {
            this.getSettings().setMixedContentMode(0);
        }
    }

    @SuppressLint(value={"NewApi"})
    public void callOnResume() {
        if (this.hasHoneycomb()) {
            super.onResume();
        }
    }

    @SuppressLint(value={"NewApi"})
    public void callOnPause() {
        if (this.hasHoneycomb()) {
            super.onPause();
        }
    }

    public void callOnDestroy() {
        this.loadUrl("file:///android_asset/nonexistent.html");
        this.clearCache(true);
        this.freeMemory();
    }

    public void addJavascriptInterface(Object obj, String interfaceName) {
        if (TextUtils.isEmpty((CharSequence)interfaceName)) {
            return;
        }
        if (this.hasJellyBeanMR1()) {
            super.addJavascriptInterface(obj, interfaceName);
        } else {
            this.mJsInterfaceMap.put(interfaceName, obj);
        }
    }

    @SuppressLint(value={"NewApi"})
    public void removeJavascriptInterface(String interfaceName) {
        if (this.hasJellyBeanMR1()) {
            super.removeJavascriptInterface(interfaceName);
        } else {
            this.mJsInterfaceMap.remove(interfaceName);
            this.mJsStringCache = null;
            this.injectJavascriptInterfaces();
        }
    }

    @SuppressLint(value={"NewApi"})
    private boolean removeSearchBoxImpl() {
        if (this.hasHoneycomb() && !this.hasJellyBeanMR1()) {
            super.removeJavascriptInterface("searchBoxJavaBridge_");
            return true;
        }
        return false;
    }

    private void injectJavascriptInterfaces() {
        String jsString;
        if (!TextUtils.isEmpty((CharSequence)this.mJsStringCache)) {
            this.loadJavascriptInterfaces();
            return;
        }
        this.mJsStringCache = jsString = this.genJavascriptInterfacesString();
        this.loadJavascriptInterfaces();
    }

    private void injectJavascriptInterfaces(WebView webView) {
        if (webView instanceof WebViewEx) {
            this.injectJavascriptInterfaces();
        }
    }

    private void loadJavascriptInterfaces() {
        this.loadUrl(this.mJsStringCache);
    }

    private String genJavascriptInterfacesString() {
        if (this.mJsInterfaceMap.size() == 0) {
            this.mJsStringCache = null;
            return null;
        }
        Iterator<Map.Entry<String, Object>> iterator = this.mJsInterfaceMap.entrySet().iterator();
        StringBuilder script = new StringBuilder();
        script.append("javascript:(function JsAddJavascriptInterface_(){");
        try {
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                String interfaceName = entry.getKey();
                Object obj = entry.getValue();
                this.createJsMethod(interfaceName, obj, script);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        script.append("})()");
        return script.toString();
    }

    private void createJsMethod(String interfaceName, Object obj, StringBuilder script) {
        Method[] methods;
        if (TextUtils.isEmpty((CharSequence)interfaceName) || null == obj || null == script) {
            return;
        }
        Class<?> objClass = obj.getClass();
        script.append("if(typeof(window.").append(interfaceName).append(")!='undefined'){");
        script.append("    console.log('window." + interfaceName + "_js_interface_name is exist!!');");
        script.append("}else {");
        script.append("    window.").append(interfaceName).append("={");
        for (Method method : methods = objClass.getMethods()) {
            int i;
            String methodName = method.getName();
            if (this.filterMethods(methodName)) continue;
            script.append("        ").append(methodName).append(":function(");
            int argCount = method.getParameterTypes().length;
            if (argCount > 0) {
                int maxCount = argCount - 1;
                for (i = 0; i < maxCount; ++i) {
                    script.append(VAR_ARG_PREFIX).append(i).append(",");
                }
                script.append(VAR_ARG_PREFIX).append(argCount - 1);
            }
            script.append(") {");
            if (method.getReturnType() != Void.TYPE) {
                script.append("            return ").append("prompt('").append(MSG_PROMPT_HEADER).append("'+");
            } else {
                script.append("            prompt('").append(MSG_PROMPT_HEADER).append("'+");
            }
            script.append("JSON.stringify({");
            script.append(KEY_INTERFACE_NAME).append(":'").append(interfaceName).append("',");
            script.append(KEY_FUNCTION_NAME).append(":'").append(methodName).append("',");
            script.append(KEY_ARG_ARRAY).append(":[");
            if (argCount > 0) {
                int max = argCount - 1;
                for (i = 0; i < max; ++i) {
                    script.append(VAR_ARG_PREFIX).append(i).append(",");
                }
                script.append(VAR_ARG_PREFIX).append(max);
            }
            script.append("]})");
            script.append(");");
            script.append("        }, ");
        }
        script.append("    };");
        script.append("}");
    }

    private boolean handleJsInterface(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        String prefix = MSG_PROMPT_HEADER;
        if (!message.startsWith(prefix)) {
            return false;
        }
        String jsonStr = message.substring(prefix.length());
        try {
            int count;
            JSONObject jsonObj = new JSONObject(jsonStr);
            String interfaceName = jsonObj.getString(KEY_INTERFACE_NAME);
            String methodName = jsonObj.getString(KEY_FUNCTION_NAME);
            JSONArray argsArray = jsonObj.getJSONArray(KEY_ARG_ARRAY);
            Object[] args = null;
            if (null != argsArray && (count = argsArray.length()) > 0) {
                args = new Object[count];
                for (int i = 0; i < count; ++i) {
                    args[i] = argsArray.get(i);
                }
            }
            if (this.invokeJSInterfaceMethod(result, interfaceName, methodName, args)) {
                return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        result.cancel();
        return false;
    }

    private boolean invokeJSInterfaceMethod(JsPromptResult result, String interfaceName, String methodName, Object[] args) {
        boolean succeed = false;
        Object obj = this.mJsInterfaceMap.get(interfaceName);
        if (null == obj) {
            result.cancel();
            return false;
        }
        Class[] parameterTypes = null;
        int count = 0;
        if (args != null) {
            count = args.length;
        }
        if (count > 0) {
            parameterTypes = new Class[count];
            for (int i = 0; i < count; ++i) {
                parameterTypes[i] = this.getClassFromJsonObject(args[i]);
            }
        }
        try {
            Method method = obj.getClass().getMethod(methodName, parameterTypes);
            Object returnObj = method.invoke(obj, args);
            boolean isVoid = returnObj == null || returnObj.getClass() == Void.TYPE;
            String returnValue = isVoid ? "" : returnObj.toString();
            result.confirm(returnValue);
            succeed = true;
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        result.cancel();
        return succeed;
    }

    private Class<?> getClassFromJsonObject(Object obj) {
        Class<Object> cls = obj.getClass();
        cls = cls == Integer.class ? Integer.TYPE : (cls == Boolean.class ? Boolean.TYPE : String.class);
        return cls;
    }

    private boolean filterMethods(String methodName) {
        for (String method : mFilterMethods) {
            if (!method.equals(methodName)) continue;
            return true;
        }
        return false;
    }

    private boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= 11;
    }

    private boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= 17;
    }

    public static class WebChromeClientEx
    extends WebChromeClient {
        private WebViewEx mWebView;

        public WebChromeClientEx(WebViewEx webView) {
            this.mWebView = webView;
        }

        public WebViewEx getWebView() {
            return this.mWebView;
        }

        public void onProgressChanged(WebView view, int newProgress) {
            this.mWebView.injectJavascriptInterfaces(view);
            super.onProgressChanged(view, newProgress);
        }

        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            if (view instanceof WebViewEx && this.mWebView.handleJsInterface(view, url, message, defaultValue, result)) {
                return true;
            }
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        public void onReceivedTitle(WebView view, String title) {
            this.mWebView.injectJavascriptInterfaces(view);
        }
    }

    public static class WebViewClientEx
    extends CompatWebViewClient {
        private WebViewEx mWebView;

        public WebViewClientEx(WebViewEx webView) {
            this.mWebView = webView;
        }

        public WebViewEx getWebView() {
            return this.mWebView;
        }

        public void onLoadResource(WebView view, String url) {
            this.mWebView.injectJavascriptInterfaces(view);
            super.onLoadResource(view, url);
        }

        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            this.mWebView.injectJavascriptInterfaces(view);
            super.doUpdateVisitedHistory(view, url, isReload);
        }

        @Override
        public void onPageStartedCompat(WebView view, String url, Bitmap favicon) {
            this.mWebView.injectJavascriptInterfaces(view);
            super.onPageStartedCompat(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            this.mWebView.injectJavascriptInterfaces(view);
            super.onPageFinished(view, url);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            this.mWebView.injectJavascriptInterfaces(view);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }
}

