// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.model;

import io.vlingo.xoom.starter.task.template.code.storage.StorageType;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import io.vlingo.xoom.starter.task.template.code.TemplateData;

import java.io.File;
import java.util.*;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;

public class ModelTemplateData extends TemplateData {

    private final static String PACKAGE_PATTERN = "%s.%s.%s";
    private final static String PARENT_PACKAGE_NAME = "model";

    public final String name;
    public final String packageName;
    public final StorageType storageType;
    public final Map<CodeTemplateStandard, List<File>> files = new LinkedHashMap<>();
    public final List<DomainEventTemplateData> events = new ArrayList<>();
    private final CodeTemplateParameters parameters;
    private final String absolutePath;

    public ModelTemplateData(final String[] dataBlocks,
                             final String basePackage,
                             final StorageType storageType,
                             final String projectPath) {
        this.storageType = storageType;
        this.name = dataBlocks[0];
        this.packageName = resolvePackage(basePackage);
        this.parameters = loadParameters();
        this.absolutePath =
                resolveAbsolutePath(basePackage, projectPath,
                        PARENT_PACKAGE_NAME, name.toLowerCase());

        this.loadEvents(dataBlocks);
        this.loadFiles();
    }

    private void loadFiles() {
        this.files.put(AGGREGATE_PROTOCOL, Arrays.asList(protocolFile()));
        this.files.put(AGGREGATE, Arrays.asList(aggregateFile()));
        this.files.put(STATE, Arrays.asList(stateFile()));
        this.files.put(PLACEHOLDER_DOMAIN_EVENT, Arrays.asList(placeholderEventFile()));
        this.events.forEach(event -> {
            this.files.computeIfAbsent(DOMAIN_EVENT, k -> new ArrayList<>()).add(event.file());
        });
    }

    private void loadEvents(final String[] dataBlocks) {
        if(dataBlocks.length > 1) {
            Arrays.asList(dataBlocks).stream().skip(1).forEach(eventName -> {
                this.events.add(new DomainEventTemplateData(eventName, packageName, absolutePath));
            });
        }
    }

    private CodeTemplateParameters loadParameters() {
        return CodeTemplateParameters.with(AGGREGATE_PROTOCOL_NAME, name)
                .and(STORAGE_TYPE, storageType)
                .and(PACKAGE_NAME, packageName)
                .and(ENTITY_NAME, AGGREGATE.resolveClassname(name))
                .and(STATE_NAME, STATE.resolveClassname(name))
                .and(DOMAIN_EVENT_NAME, PLACEHOLDER_DOMAIN_EVENT.resolveClassname(name));
    }

    private String resolvePackage(final String basePackage) {
        return String.format(PACKAGE_PATTERN, basePackage, PARENT_PACKAGE_NAME, name).toLowerCase();
    }

    private File protocolFile() {
        return buildFile(AGGREGATE_PROTOCOL, absolutePath, name);
    }

    private File aggregateFile() {
        return buildFile(AGGREGATE, absolutePath, name);
    }

    private File stateFile() {
        return buildFile(STATE, absolutePath, name);
    }

    private File placeholderEventFile() {
        return buildFile(PLACEHOLDER_DOMAIN_EVENT, absolutePath, name);
    }

    @Override
    public CodeTemplateParameters templateParameters() {
        return parameters;
    }

    @Override
    public File file() {
        throw new UnsupportedOperationException("This operation is not supported");
    }
}
