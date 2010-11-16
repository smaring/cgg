/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cgg.scripts;

import java.io.FileReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 *
 * @author pdpi
 */
class BaseScope extends ImporterTopLevel {

    protected static final Log logger = LogFactory.getLog(BaseScope.class);
    protected boolean sealedStdLib = false;
    boolean initialized;
    protected String basePath, systemPath;

    public BaseScope() {
        super();
    }

    public void init(Context cx) {
        // Define some global functions particular to the shell. Note
        // that these functions are not part of ECMA.
        initStandardObjects(cx, sealedStdLib);
        String[] names = {
            "print", "load", "lib"};
        defineFunctionProperties(names, BaseScope.class,
                ScriptableObject.DONTENUM);

        initialized = true;
    }

    public static Object print(Context cx, Scriptable thisObj,
            Object[] args, Function funObj) {

        for (Object arg : args) {
            String s = Context.toString(arg);
            logger.info(s);
        }
        return Context.getUndefinedValue();
    }

    public static Object load(Context cx, Scriptable thisObj,
            Object[] args, Function funObj) {

        String file = args[0].toString();
        try {
            BaseScope scope = (BaseScope) thisObj;
            cx.evaluateReader(scope, new FileReader(scope.basePath + "/" + file), "<file>", 1, null);
        } catch (Exception e) {
            logger.error(e);
            return Context.toBoolean(false);
        }
        return Context.toBoolean(true);
    }

        public static Object lib(Context cx, Scriptable thisObj,
            Object[] args, Function funObj) {

        String file = args[0].toString();
        try {
            BaseScope scope = (BaseScope) thisObj;
            cx.evaluateReader(scope, new FileReader(scope.systemPath + "/" + file), "<file>", 1, null);
        } catch (Exception e) {
            logger.error(e);
            return Context.toBoolean(false);
        }
        return Context.toBoolean(true);
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setSystemPath(String systemPath) {
        this.systemPath = systemPath;
    }
}
