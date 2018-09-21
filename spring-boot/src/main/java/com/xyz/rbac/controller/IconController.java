package com.xyz.rbac.controller;

import com.xyz.rbac.result.JSONResult;
import com.xyz.rbac.util.HttpUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@ResponseBody
@RequestMapping("/api/icon")
public class IconController {

    @GetMapping("/")
    public JSONResult index() {
        String css=HttpUtil.get("http://127.0.0.1:8080/static/fontawesome/css/fontawesome-all.min.css", "utf-8");
        Pattern regex = Pattern.compile("\\.(fa(-[a-zA-Z0-9]*)+):before");
        Matcher matcher=regex.matcher(css);
        List<String> list=new ArrayList<String>();
        while (matcher.find()){
            list.add(matcher.group(1));
        }
        return JSONResult.success(list);
    }
}
