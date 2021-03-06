// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration;

import io.vlingo.xoom.starter.task.TaskExecutionContext;

import java.util.List;

import static io.vlingo.xoom.starter.task.Agent.TERMINAL;

public class CommandLineBasedProjectGenerationManager extends ProjectGenerationManager<List<String>> {

    @Override
    public void run(final List<String> args) {
        processSteps(TaskExecutionContext.executedFrom(TERMINAL).withArgs(args));
    }

}
