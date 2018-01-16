package com.swallow.sell.util;

import com.swallow.sell.vo.ResultVO;

/**
 * @author jdd
 */
public class ResultVOUtil {


    /**
     * 成功操作 带返回值
     * @param data
     * @return
     */
    public static ResultVO success(Object data){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(data);
        return resultVO;
    }

    /**
     * 不带参数的 成功操作
     * @return
     */
    public static ResultVO success(){
        return success(null);
    }

    /**
     * 错误返回
     * @param code
     * @param msg
     * @return
     */
    public static ResultVO error(Integer code,String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
