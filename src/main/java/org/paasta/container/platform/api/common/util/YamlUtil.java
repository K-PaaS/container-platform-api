package org.paasta.container.platform.api.common.util;

import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.model.CommonStatusCode;
import org.paasta.container.platform.api.exception.ContainerPlatformException;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.Map;

public class YamlUtil {

    /**
     * keyword 'kind'로 yaml의 Resource 값 구하기
     *
     * @param yaml
     * @param keyword
     * @return
     */
    public static String parsingYaml(String yaml, String keyword) {
        String value = null;
        try {
            Yaml y = new Yaml();
            Map<String,Object> yamlMap = y.load(yaml);

            if("kind".equals(keyword)) {
                value = (String) yamlMap.get(keyword);
            } else if("metadata".equals(keyword)) {
                Map a = (Map) yamlMap.get(keyword);
                value = a.get("name").toString();
            }
            
        } catch (ClassCastException e) {
            throw new ContainerPlatformException(Constants.RESULT_STATUS_FAIL, CommonStatusCode.INVALID_FORMAT.getMsg(), CommonStatusCode.INVALID_FORMAT.getCode(), CommonStatusCode.INVALID_FORMAT.getMsg());
        }

        return value;
    }

    /**
     * url Resource 값과 비교할 yaml의 Resource 값 추출
     *
     * @param kind
     * @return YamlKind
     */
    public static String makeResourceNameYAML(String kind) {
        String YamlKind =  kind.toLowerCase();
        return YamlKind;
    }


   /**
    * 복합 yaml '---'로 나누어 list화
    * trim 사용하여 공백 제거
    * @param yaml
    * @return
    */
    public static String[] splitYaml(String yaml) {
        String[] yamlArray = yaml.split("---");
        ArrayList<String> returnList = new ArrayList<String>();

        for (String temp : yamlArray) {
            temp =  temp.trim();
            if ( temp.length() > 0 )  {
                returnList.add(temp);
            }
        }
         return returnList.toArray(new String[returnList.size()]);
    }


}
