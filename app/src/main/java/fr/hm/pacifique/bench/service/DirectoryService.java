package fr.hm.pacifique.bench.service;

import fr.hm.pacifique.bench.config.PacifiqueProperties;
import fr.hm.pacifique.bench.constant.BatchConstant;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.projection.common.Context;
import fr.hm.pacifique.common.constant.CommonConstants;
import fr.hm.pacifique.common.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class DirectoryService {
    private final PacifiqueProperties pacifiqueProperties;
    private final FileService fileService;
    private Application application;
    private HalfFlow halfFlow;
    private AppType appType;

    public void init(Application application, HalfFlow halfFlow) {
        this.application = application;
        this.halfFlow = halfFlow;

        fileService.createDirectory(getArcInDataDirectory());
        fileService.createDirectory(getArcInUserDirectory());
        fileService.createDirectory(getArcOutDirectory());
        fileService.createDirectory(getInDataDirectory());
        fileService.createDirectory(getInUserDirectory());
        fileService.createDirectory(getOutDirectory());
        fileService.createDirectory(getLogDirectory());
        fileService.createDirectory(getWorkDirectory());
    }

    public void init(Object... objets) {
        for (Object object : objets) {
            if (object instanceof Application) {
                this.application = (Application) object;
            } else if (object instanceof HalfFlow) {
                this.halfFlow = (HalfFlow) object;
            } else if (object instanceof AppType) {
                this.appType = (AppType) object;
            }
        }
    }

    private String getPath(String... paths) {
        String path = Paths.get(Paths.get(pacifiqueProperties.getRootDirectory(), appType.getDirectory(), application.getDirectory()).toString(), paths).toString();
        fileService.createDirectory(path);
        return path;
    }

    public String getTriggerDirectory(Object... objects) {
        init(objects);
        return getOutDirectory();
    }

    public String getOutputDirectory(Object... objects) {
        init(objects);
        return getInDataDirectory();
    }

    public String getArcInDataDirectory(Object... objects) {
        init(objects);
        return getPath(CommonConstants.ARC_IN_DATA_DIRECTORY, halfFlow.getDirectory());
    }

    public String getArcInUserDirectory(Object... objects) {
        init(objects);
        return getPath(CommonConstants.ARC_IN_USER_DIRECTORY);
    }

    public String getArcOutDirectory(Object... objects) {
        init(objects);
        return getPath(CommonConstants.ARC_OUT_DIRECTORY, halfFlow.getDirectory());
    }

    public String getInDataDirectory(Object... objects) {
        init(objects);
        return getPath(CommonConstants.IN_DATA_DIRECTORY, halfFlow.getDirectory());
    }

    public String getInDataDirectory(AppType appType, Context context) {
        init(appType);
        return getPath(CommonConstants.IN_DATA_DIRECTORY, halfFlow.getDirectory(), context == null ? "" : context.getDirectory());
    }

    public String getInUserDirectory(Object... objects) {
        init(objects);
        return getPath(CommonConstants.IN_USER_DIRECTORY);
    }

    public String getOutDirectory(Object... objects) {
        init(objects);
        return getPath(CommonConstants.OUT_DIRECTORY, halfFlow.getDirectory());
    }

    public String getLogDirectory(Object... objects) {
        init(objects);
        return getPath(CommonConstants.LOG_DIRECTORY);
    }

    public String getWorkDirectory(Object... objects) {
        init(objects);
        return getPath(CommonConstants.WORK_DIRECTORY, BatchConstant.PACIFIQUE_DIRECTORY, halfFlow.getDirectory());
    }
}
