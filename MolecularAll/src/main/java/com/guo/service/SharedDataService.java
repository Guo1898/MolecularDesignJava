package com.guo.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
public class SharedDataService {

    private String NewFolder;
    private String Filename;

    public String getNewFolder() {
        return NewFolder;
    }

    public void setNewFolder(String newFolder) {
        this.NewFolder = newFolder;
    }

    public String getFilename() {
        return Filename;
    }

    public void setFilename(String filename) {
        this.Filename = filename;
    }
}
