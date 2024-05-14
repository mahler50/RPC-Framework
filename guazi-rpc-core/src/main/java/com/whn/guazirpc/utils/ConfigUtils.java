package com.whn.guazirpc.utils;

import cn.hutool.core.io.resource.NoResourceException;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.dialect.Props;
import cn.hutool.setting.yaml.YamlUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 配置工具类
 */
@Slf4j
public class ConfigUtils {

    private static final String BASE_PATH_DIR = "conf/";

    private static final String BASE_CONF_FILE_NAME = "application";

    private static final String PROPERTIES_FILE_SUFFIX = ".properties";

    private static final String YAML_FILE_SUFFIX = ".yaml";

    private static final String YML_FILE_SUFFIX = ".yml";

    private static final String ENV_SPLIT = "-";

    /**
     * 加载配置对象
     * @param clazz
     * @param prefix
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> clazz, String prefix) {
        return loadConfig(clazz, prefix, "");
    }

    /**
     * 加载配置
     * <p>
     *     优先加载 properties， 然后加载 yml/yaml
     * @param clazz
     * @param prefix
     * @param env
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> clazz, String prefix, String env) {
        T props;
        return ((props = loadProperties(clazz, prefix, env)) != null ? props
                : loadYaml(clazz, prefix, env));
    }


    /**
     * 加载 properties 配置文件
     * <p>
     *     优先加载 conf/application.properties， 如果不存在则加载 application.properties
     * @param clazz
     * @param prefix
     * @param env
     * @return
     * @param <T>
     */
    public static <T> T loadProperties(Class<T> clazz, String prefix, String env) {
        try {
            return doLoadProperties(clazz, BASE_PATH_DIR + BASE_CONF_FILE_NAME, prefix, env);
        } catch (NoResourceException e) {
            log.warn("[{}]路径下不存在以 properties 为扩展名的配置文件, 即将从类路径下加载", BASE_PATH_DIR);
        }
        try {
            return doLoadProperties(clazz, BASE_CONF_FILE_NAME, prefix, env);
        } catch (NoResourceException e) {
            log.warn("类路径下不存在以 properties 为扩展名的配置文件，即将加载 yml/yaml 文件");
        }
        return null;
    }

    /**
     * 加载 yml/yaml 配置文件
     * <p>
     *     加载顺序为：conf/application.yaml > application.yaml
     *     > config/application.yml > application.yml
     *
     * @param clazz
     * @param prefix
     * @param env
     * @return
     * @param <T>
     */
    public static <T> T loadYaml(Class<T> clazz, String prefix, String env) {
        try {
            return doLoadYaml(clazz, BASE_PATH_DIR + BASE_CONF_FILE_NAME, prefix, env, YAML_FILE_SUFFIX);
        } catch (NoResourceException e) {
            log.warn("[{}]路径下不存在以 yaml 为扩展名的配置文件，即将从类路径下加载", BASE_PATH_DIR);
        }
        try {
            return doLoadYaml(clazz, BASE_CONF_FILE_NAME, prefix, env, YAML_FILE_SUFFIX);
        } catch (NoResourceException e) {
            log.warn("类路径下不存在以 yaml 为扩展名的配置文件，即将加载 yml 文件");
        }
        try {
            return doLoadYaml(clazz, BASE_PATH_DIR + BASE_CONF_FILE_NAME, prefix, env, YML_FILE_SUFFIX);
        } catch (NoResourceException e) {
            log.warn("[{}]路径下不存在以 yml 为扩展名的配置文件，即将从类路径下加载", BASE_PATH_DIR);
        }
        try {
            return doLoadYaml(clazz, BASE_CONF_FILE_NAME, prefix, env, YML_FILE_SUFFIX);
        } catch (NoResourceException e) {
            log.error("不存在配置文件");
            throw e;
        }
    }

    /**
     * 加载 properties 文件配置 eg：application-{env}.properties
     * @param clazz
     * @param basePath
     * @param prefix
     * @param env
     * @return
     * @param <T>
     */
    private static <T> T doLoadProperties(Class<T> clazz, String basePath, String prefix, String env)
            throws NoResourceException {
        String configFilePath = buildConfigFilePath(basePath, env, PROPERTIES_FILE_SUFFIX);
        Props props = new Props(configFilePath);
        return props.toBean(clazz, prefix);
    }

    /**
     * 加载 yaml 文件配置 eg:application-{env}.yaml
     * @param clazz
     * @param basePath
     * @param prefix
     * @param env
     * @param suffix
     * @return
     * @param <T>
     */
    private static <T> T doLoadYaml(Class<T> clazz, String basePath, String prefix, String env, String suffix)
            throws NoResourceException{
        String configFilePath = buildConfigFilePath(basePath, env, suffix);
        Map<String, Object> props = YamlUtil.loadByPath(configFilePath);
        JSONObject rpcConfigProps = JSONUtil.parseObj(props).getJSONObject(prefix);
        return JSONUtil.toBean(rpcConfigProps, clazz);
    }

    /**
     * 构造配置文件路径
     * @param basePath 基础路径
     * @param env 环境
     * @param suffix 后缀
     * @return
     */
    private static String buildConfigFilePath(String basePath, String env, String suffix) {
        StringBuilder stringBuilder = new StringBuilder(basePath);
        if (StrUtil.isNotBlank(env)) {
            stringBuilder.append(ENV_SPLIT).append(env);
        }
        stringBuilder.append(suffix);
        return stringBuilder.toString();
    }
}
