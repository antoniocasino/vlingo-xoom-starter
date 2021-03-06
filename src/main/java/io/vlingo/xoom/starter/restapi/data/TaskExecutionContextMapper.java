// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.parameter.Label;
import io.vlingo.xoom.codegen.template.exchange.ExchangeRole;
import io.vlingo.xoom.starter.Configuration;
import io.vlingo.xoom.starter.task.TaskExecutionContext;

import static io.vlingo.xoom.codegen.parameter.Label.*;
import static io.vlingo.xoom.starter.task.Agent.WEB;

public class TaskExecutionContextMapper {

    private final GenerationSettingsData data;
    private final CodeGenerationParameters parameters;

    public static TaskExecutionContext from(final GenerationSettingsData data) {
        return new TaskExecutionContextMapper(data).map();
    }

    private TaskExecutionContextMapper(final GenerationSettingsData data) {
        this.data = data;
        this.parameters = CodeGenerationParameters.empty();
        mapAggregates(); mapPersistence(); mapStructuralOptions();
    }

    private TaskExecutionContext map() {
        return TaskExecutionContext.executedFrom(WEB).with(parameters);
    }

    private void mapAggregates() {
        data.model.aggregateSettings.forEach(aggregate -> {
            final CodeGenerationParameter aggregateParameter =
                    CodeGenerationParameter.of(AGGREGATE, aggregate.aggregateName)
                            .relate(URI_ROOT, aggregate.api.rootPath);

            mapStateFields(aggregate, aggregateParameter);
            mapDomainEvents(aggregate, aggregateParameter);
            mapMethods(aggregate, aggregateParameter);
            mapRoutes(aggregate, aggregateParameter);
            mapExchanges(aggregate, aggregateParameter);
            parameters.add(aggregateParameter);
        });
    }

    private void mapStateFields(final AggregateData aggregateData,
                                final CodeGenerationParameter aggregateParameter) {
        aggregateData.stateFields.forEach(stateField -> {
            aggregateParameter.relate(
                    CodeGenerationParameter.of(STATE_FIELD, stateField.name)
                            .relate(FIELD_TYPE, stateField.type));
        });
    }

    private void mapMethods(final AggregateData aggregateData,
                            final CodeGenerationParameter aggregateParameter) {
        aggregateData.methods.forEach(method -> {
            final CodeGenerationParameter methodParameter =
                    CodeGenerationParameter.of(AGGREGATE_METHOD, method.name)
                            .relate(FACTORY_METHOD, method.useFactory)
                            .relate(DOMAIN_EVENT, method.event);

            method.parameters.forEach(param -> methodParameter.relate(METHOD_PARAMETER, param));

            aggregateParameter.relate(methodParameter);
        });
    }

    private void mapDomainEvents(final AggregateData aggregateData,
                                 final CodeGenerationParameter aggregateParameter) {
        aggregateData.events.forEach(event -> {
            final CodeGenerationParameter eventParameter =
                    CodeGenerationParameter.of(DOMAIN_EVENT, event.name);

            event.fields.forEach(field -> {
                eventParameter.relate(STATE_FIELD, field);
            });

            aggregateParameter.relate(eventParameter);
        });
    }

    private void mapRoutes(final AggregateData aggregateData,
                           final CodeGenerationParameter aggregateParameter) {
        aggregateData.api.routes.forEach(route -> {
            final CodeGenerationParameter routeParameter =
                    CodeGenerationParameter.of(ROUTE_SIGNATURE, route.aggregateMethod)
                            .relate(ROUTE_METHOD, route.httpMethod)
                            .relate(ROUTE_PATH, route.path)
                            .relate(REQUIRE_ENTITY_LOADING, route.requireEntityLoad);

            aggregateParameter.relate(routeParameter);
        });
    }

    private void mapExchanges(final AggregateData aggregate,
                              final CodeGenerationParameter aggregateParameter) {
        if(aggregate.hasConsumerExchange()) {
            final CodeGenerationParameter consumerExchange =
                    CodeGenerationParameter.of(Label.EXCHANGE, aggregate.consumerExchange.exchangeName)
                            .relate(Label.ROLE, ExchangeRole.CONSUMER);

            aggregate.consumerExchange.receivers.forEach(receiver -> {
                consumerExchange.relate(CodeGenerationParameter.of(RECEIVER)
                        .relate(MODEL_METHOD, receiver.aggregateMethod)
                        .relate(SCHEMA, receiver.schema));
            });
            aggregateParameter.relate(consumerExchange);
        }
        if(aggregate.hasProducerExchange()) {
            final CodeGenerationParameter producerExchange =
                    CodeGenerationParameter.of(Label.EXCHANGE, aggregate.producerExchange.exchangeName)
                            .relate(Label.SCHEMA_GROUP, aggregate.producerExchange.schemaGroup)
                            .relate(Label.ROLE, ExchangeRole.PRODUCER);

            aggregate.producerExchange.outgoingEvents.forEach(eventName -> producerExchange.relate(DOMAIN_EVENT, eventName));

            aggregateParameter.relate(producerExchange);
        }
    }

    private void mapPersistence() {
        parameters.add(CQRS, data.model.persistenceSettings.useCQRS)
                .add(DATABASE, data.model.persistenceSettings.database)
                .add(PROJECTION_TYPE, data.model.persistenceSettings.projections)
                .add(STORAGE_TYPE, data.model.persistenceSettings.storageType)
                .add(COMMAND_MODEL_DATABASE, data.model.persistenceSettings.commandModelDatabase)
                .add(QUERY_MODEL_DATABASE, data.model.persistenceSettings.queryModelDatabase);
    }

    private void mapStructuralOptions() {
        parameters.add(APPLICATION_NAME, data.context.artifactId)
                .add(USE_ANNOTATIONS, data.useAnnotations)
                .add(USE_AUTO_DISPATCH, data.useAutoDispatch)
                .add(GROUP_ID, data.context.groupId)
                .add(ARTIFACT_ID, data.context.artifactId)
                .add(VERSION, data.context.artifactVersion)
                .add(PACKAGE, data.context.packageName)
                .add(XOOM_VERSION, Configuration.resolveDefaultXoomVersion())
                .add(DEPLOYMENT, data.deployment.type)
                .add(DOCKER_IMAGE, data.deployment.dockerImage)
                .add(KUBERNETES_IMAGE, data.deployment.kubernetesImage)
                .add(KUBERNETES_POD_NAME, data.deployment.kubernetesPod)
                .add(TARGET_FOLDER, data.projectDirectory);
    }
}
