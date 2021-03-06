// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.archetype;

import io.vlingo.xoom.starter.task.projectgeneration.ProjectGenerationException;

public class ArchetypeNotFoundException extends ProjectGenerationException {

    public ArchetypeNotFoundException() {
        super("Unable to find a Template based on properties. Please check if it contains all required properties.");
    }
}
