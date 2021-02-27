// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.codegen.parameter.Label;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.CommandResolverStep;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AngularSchematicExecutionStep extends CommandResolverStep {

  @Override
  protected String formatCommands(final TaskExecutionContext context) {
    // might be useful for xoom "gen" param
    //boolean angularApp = context.hasProperty(Property.ANGULAR_APP);
    /** Commands list:
        1. check node is installed , otherwise give an alert
        2. check angular is installed otherwise give an alert <code>npm install -g @angular/cli</code>
        3. create angular app
    **/
    /*
      final String schematicCommand = "ng schematic";
      final String temporaryProjectPath = context.temporaryProjectPath();
      final String projectFolderCommand = resolveDirectoryChangeCommand(temporaryProjectPath);
    */

    final String artifactId = context.codeGenerationParameters().retrieveValue(Label.ARTIFACT_ID);
    final List<String> commands = Arrays.asList(/*projectFolderCommand,*/"node -v", "ng version","ng new "+artifactId);
    return commands.stream().collect(Collectors.joining(" && "));
  }

}
