
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "PropertyName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RedundantCompanionReference",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "unused",
)

package com.google.firebase.dataconnect.generated

import com.google.firebase.dataconnect.getInstance as _fdcGetInstance
import kotlin.time.Duration.Companion.milliseconds as _milliseconds

public interface ExampleConnector : com.google.firebase.dataconnect.generated.GeneratedConnector<ExampleConnector> {
  override val dataConnect: com.google.firebase.dataconnect.FirebaseDataConnect

  
    public val createAlerts: CreateAlertsMutation
  
    public val createLocation: CreateLocationMutation
  
    public val createMetric: CreateMetricMutation
  
    public val createProject: CreateProjectMutation
  
    public val createSensor: CreateSensorMutation
  
    public val deleteAlert: DeleteAlertMutation
  
    public val deleteLocation: DeleteLocationMutation
  
    public val deleteMetric: DeleteMetricMutation
  
    public val deleteProject: DeleteProjectMutation
  
    public val deleteSensor: DeleteSensorMutation
  
    public val getAlert: GetAlertQuery
  
    public val getLocation: GetLocationQuery
  
    public val getMetric: GetMetricQuery
  
    public val getProject: GetProjectQuery
  
    public val getSensor: GetSensorQuery
  
    public val listAlerts: ListAlertsQuery
  
    public val listLocations: ListLocationsQuery
  
    public val listMetrics: ListMetricsQuery
  
    public val listProjects: ListProjectsQuery
  
    public val listSensors: ListSensorsQuery
  
    public val updateAlert: UpdateAlertMutation
  
    public val updateLocation: UpdateLocationMutation
  
    public val updateMetric: UpdateMetricMutation
  
    public val updateProject: UpdateProjectMutation
  
    public val updateSensor: UpdateSensorMutation
  

  public companion object {
    @Suppress("MemberVisibilityCanBePrivate")
    public val config: com.google.firebase.dataconnect.ConnectorConfig = com.google.firebase.dataconnect.ConnectorConfig(
      connector = "example",
      location = "us-east4",
      serviceId = "appsensorproximidadiot",
    )

    public fun getInstance(
      dataConnect: com.google.firebase.dataconnect.FirebaseDataConnect
    ):ExampleConnector = synchronized(instances) {
      instances.getOrPut(dataConnect) {
        ExampleConnectorImpl(dataConnect)
      }
    }

    private val instances = java.util.WeakHashMap<com.google.firebase.dataconnect.FirebaseDataConnect, ExampleConnectorImpl>()

    
    public val defaultCacheSettings: com.google.firebase.dataconnect.CacheSettings =
      com.google.firebase.dataconnect.CacheSettings(
        
        
      )

    public val defaultDataConnectSettings: com.google.firebase.dataconnect.DataConnectSettings =
      com.google.firebase.dataconnect.DataConnectSettings(
        cacheSettings = defaultCacheSettings,
      )
    
  }
}

public val ExampleConnector.Companion.instance:ExampleConnector
  get() = getInstance(com.google.firebase.dataconnect.FirebaseDataConnect._fdcGetInstance(
    config, defaultDataConnectSettings
  ))

public fun ExampleConnector.Companion.getInstance(
  settings: com.google.firebase.dataconnect.DataConnectSettings = defaultDataConnectSettings
):ExampleConnector =
  getInstance(com.google.firebase.dataconnect.FirebaseDataConnect._fdcGetInstance(config, settings))

public fun ExampleConnector.Companion.getInstance(
  app: com.google.firebase.FirebaseApp,
  settings: com.google.firebase.dataconnect.DataConnectSettings = defaultDataConnectSettings
):ExampleConnector =
  getInstance(com.google.firebase.dataconnect.FirebaseDataConnect._fdcGetInstance(app, config, settings))

private class ExampleConnectorImpl(
  override val dataConnect: com.google.firebase.dataconnect.FirebaseDataConnect
) : ExampleConnector {
  
    override val createAlerts by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateAlertsMutationImpl(this)
    }
  
    override val createLocation by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateLocationMutationImpl(this)
    }
  
    override val createMetric by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateMetricMutationImpl(this)
    }
  
    override val createProject by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateProjectMutationImpl(this)
    }
  
    override val createSensor by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateSensorMutationImpl(this)
    }
  
    override val deleteAlert by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteAlertMutationImpl(this)
    }
  
    override val deleteLocation by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteLocationMutationImpl(this)
    }
  
    override val deleteMetric by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteMetricMutationImpl(this)
    }
  
    override val deleteProject by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteProjectMutationImpl(this)
    }
  
    override val deleteSensor by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteSensorMutationImpl(this)
    }
  
    override val getAlert by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetAlertQueryImpl(this)
    }
  
    override val getLocation by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetLocationQueryImpl(this)
    }
  
    override val getMetric by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetMetricQueryImpl(this)
    }
  
    override val getProject by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetProjectQueryImpl(this)
    }
  
    override val getSensor by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetSensorQueryImpl(this)
    }
  
    override val listAlerts by lazy(LazyThreadSafetyMode.PUBLICATION) {
      ListAlertsQueryImpl(this)
    }
  
    override val listLocations by lazy(LazyThreadSafetyMode.PUBLICATION) {
      ListLocationsQueryImpl(this)
    }
  
    override val listMetrics by lazy(LazyThreadSafetyMode.PUBLICATION) {
      ListMetricsQueryImpl(this)
    }
  
    override val listProjects by lazy(LazyThreadSafetyMode.PUBLICATION) {
      ListProjectsQueryImpl(this)
    }
  
    override val listSensors by lazy(LazyThreadSafetyMode.PUBLICATION) {
      ListSensorsQueryImpl(this)
    }
  
    override val updateAlert by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateAlertMutationImpl(this)
    }
  
    override val updateLocation by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateLocationMutationImpl(this)
    }
  
    override val updateMetric by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateMetricMutationImpl(this)
    }
  
    override val updateProject by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateProjectMutationImpl(this)
    }
  
    override val updateSensor by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateSensorMutationImpl(this)
    }
  

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun operations(): List<com.google.firebase.dataconnect.generated.GeneratedOperation<ExampleConnector, *, *>> =
    queries() + mutations()

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun mutations(): List<com.google.firebase.dataconnect.generated.GeneratedMutation<ExampleConnector, *, *>> =
    listOf(
      createAlerts,
        createLocation,
        createMetric,
        createProject,
        createSensor,
        deleteAlert,
        deleteLocation,
        deleteMetric,
        deleteProject,
        deleteSensor,
        updateAlert,
        updateLocation,
        updateMetric,
        updateProject,
        updateSensor,
        
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun queries(): List<com.google.firebase.dataconnect.generated.GeneratedQuery<ExampleConnector, *, *>> =
    listOf(
      getAlert,
        getLocation,
        getMetric,
        getProject,
        getSensor,
        listAlerts,
        listLocations,
        listMetrics,
        listProjects,
        listSensors,
        
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun copy(dataConnect: com.google.firebase.dataconnect.FirebaseDataConnect) =
    ExampleConnectorImpl(dataConnect)

  override fun equals(other: Any?): Boolean =
    other is ExampleConnectorImpl &&
    other.dataConnect == dataConnect

  override fun hashCode(): Int =
    java.util.Objects.hash(
      "ExampleConnectorImpl",
      dataConnect,
    )

  override fun toString(): String =
    "ExampleConnectorImpl(dataConnect=$dataConnect)"
}



private open class ExampleConnectorGeneratedQueryImpl<Data, Variables>(
  override val connector: ExampleConnector,
  override val operationName: String,
  override val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data>,
  override val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables>,
) : com.google.firebase.dataconnect.generated.GeneratedQuery<ExampleConnector, Data, Variables> {

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun copy(
    connector: ExampleConnector,
    operationName: String,
    dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data>,
    variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables>,
  ) =
    ExampleConnectorGeneratedQueryImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun <NewVariables> withVariablesSerializer(
    variablesSerializer: kotlinx.serialization.SerializationStrategy<NewVariables>
  ) =
    ExampleConnectorGeneratedQueryImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun <NewData> withDataDeserializer(
    dataDeserializer: kotlinx.serialization.DeserializationStrategy<NewData>
  ) =
    ExampleConnectorGeneratedQueryImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun equals(other: Any?): Boolean =
    other is ExampleConnectorGeneratedQueryImpl<*,*> &&
    other.connector == connector &&
    other.operationName == operationName &&
    other.dataDeserializer == dataDeserializer &&
    other.variablesSerializer == variablesSerializer

  override fun hashCode(): Int =
    java.util.Objects.hash(
      "ExampleConnectorGeneratedQueryImpl",
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun toString(): String =
    "ExampleConnectorGeneratedQueryImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

private open class ExampleConnectorGeneratedMutationImpl<Data, Variables>(
  override val connector: ExampleConnector,
  override val operationName: String,
  override val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data>,
  override val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables>,
) : com.google.firebase.dataconnect.generated.GeneratedMutation<ExampleConnector, Data, Variables> {

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun copy(
    connector: ExampleConnector,
    operationName: String,
    dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data>,
    variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables>,
  ) =
    ExampleConnectorGeneratedMutationImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun <NewVariables> withVariablesSerializer(
    variablesSerializer: kotlinx.serialization.SerializationStrategy<NewVariables>
  ) =
    ExampleConnectorGeneratedMutationImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun <NewData> withDataDeserializer(
    dataDeserializer: kotlinx.serialization.DeserializationStrategy<NewData>
  ) =
    ExampleConnectorGeneratedMutationImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun equals(other: Any?): Boolean =
    other is ExampleConnectorGeneratedMutationImpl<*,*> &&
    other.connector == connector &&
    other.operationName == operationName &&
    other.dataDeserializer == dataDeserializer &&
    other.variablesSerializer == variablesSerializer

  override fun hashCode(): Int =
    java.util.Objects.hash(
      "ExampleConnectorGeneratedMutationImpl",
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun toString(): String =
    "ExampleConnectorGeneratedMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}



private class CreateAlertsMutationImpl(
  connector: ExampleConnector
):
  CreateAlertsMutation,
  ExampleConnectorGeneratedMutationImpl<
      CreateAlertsMutation.Data,
      Unit
  >(
    connector,
    CreateAlertsMutation.Companion.operationName,
    CreateAlertsMutation.Companion.dataDeserializer,
    CreateAlertsMutation.Companion.variablesSerializer,
  )


private class CreateLocationMutationImpl(
  connector: ExampleConnector
):
  CreateLocationMutation,
  ExampleConnectorGeneratedMutationImpl<
      CreateLocationMutation.Data,
      Unit
  >(
    connector,
    CreateLocationMutation.Companion.operationName,
    CreateLocationMutation.Companion.dataDeserializer,
    CreateLocationMutation.Companion.variablesSerializer,
  )


private class CreateMetricMutationImpl(
  connector: ExampleConnector
):
  CreateMetricMutation,
  ExampleConnectorGeneratedMutationImpl<
      CreateMetricMutation.Data,
      Unit
  >(
    connector,
    CreateMetricMutation.Companion.operationName,
    CreateMetricMutation.Companion.dataDeserializer,
    CreateMetricMutation.Companion.variablesSerializer,
  )


private class CreateProjectMutationImpl(
  connector: ExampleConnector
):
  CreateProjectMutation,
  ExampleConnectorGeneratedMutationImpl<
      CreateProjectMutation.Data,
      Unit
  >(
    connector,
    CreateProjectMutation.Companion.operationName,
    CreateProjectMutation.Companion.dataDeserializer,
    CreateProjectMutation.Companion.variablesSerializer,
  )


private class CreateSensorMutationImpl(
  connector: ExampleConnector
):
  CreateSensorMutation,
  ExampleConnectorGeneratedMutationImpl<
      CreateSensorMutation.Data,
      Unit
  >(
    connector,
    CreateSensorMutation.Companion.operationName,
    CreateSensorMutation.Companion.dataDeserializer,
    CreateSensorMutation.Companion.variablesSerializer,
  )


private class DeleteAlertMutationImpl(
  connector: ExampleConnector
):
  DeleteAlertMutation,
  ExampleConnectorGeneratedMutationImpl<
      DeleteAlertMutation.Data,
      DeleteAlertMutation.Variables
  >(
    connector,
    DeleteAlertMutation.Companion.operationName,
    DeleteAlertMutation.Companion.dataDeserializer,
    DeleteAlertMutation.Companion.variablesSerializer,
  )


private class DeleteLocationMutationImpl(
  connector: ExampleConnector
):
  DeleteLocationMutation,
  ExampleConnectorGeneratedMutationImpl<
      DeleteLocationMutation.Data,
      DeleteLocationMutation.Variables
  >(
    connector,
    DeleteLocationMutation.Companion.operationName,
    DeleteLocationMutation.Companion.dataDeserializer,
    DeleteLocationMutation.Companion.variablesSerializer,
  )


private class DeleteMetricMutationImpl(
  connector: ExampleConnector
):
  DeleteMetricMutation,
  ExampleConnectorGeneratedMutationImpl<
      DeleteMetricMutation.Data,
      DeleteMetricMutation.Variables
  >(
    connector,
    DeleteMetricMutation.Companion.operationName,
    DeleteMetricMutation.Companion.dataDeserializer,
    DeleteMetricMutation.Companion.variablesSerializer,
  )


private class DeleteProjectMutationImpl(
  connector: ExampleConnector
):
  DeleteProjectMutation,
  ExampleConnectorGeneratedMutationImpl<
      DeleteProjectMutation.Data,
      DeleteProjectMutation.Variables
  >(
    connector,
    DeleteProjectMutation.Companion.operationName,
    DeleteProjectMutation.Companion.dataDeserializer,
    DeleteProjectMutation.Companion.variablesSerializer,
  )


private class DeleteSensorMutationImpl(
  connector: ExampleConnector
):
  DeleteSensorMutation,
  ExampleConnectorGeneratedMutationImpl<
      DeleteSensorMutation.Data,
      DeleteSensorMutation.Variables
  >(
    connector,
    DeleteSensorMutation.Companion.operationName,
    DeleteSensorMutation.Companion.dataDeserializer,
    DeleteSensorMutation.Companion.variablesSerializer,
  )


private class GetAlertQueryImpl(
  connector: ExampleConnector
):
  GetAlertQuery,
  ExampleConnectorGeneratedQueryImpl<
      GetAlertQuery.Data,
      GetAlertQuery.Variables
  >(
    connector,
    GetAlertQuery.Companion.operationName,
    GetAlertQuery.Companion.dataDeserializer,
    GetAlertQuery.Companion.variablesSerializer,
  )


private class GetLocationQueryImpl(
  connector: ExampleConnector
):
  GetLocationQuery,
  ExampleConnectorGeneratedQueryImpl<
      GetLocationQuery.Data,
      GetLocationQuery.Variables
  >(
    connector,
    GetLocationQuery.Companion.operationName,
    GetLocationQuery.Companion.dataDeserializer,
    GetLocationQuery.Companion.variablesSerializer,
  )


private class GetMetricQueryImpl(
  connector: ExampleConnector
):
  GetMetricQuery,
  ExampleConnectorGeneratedQueryImpl<
      GetMetricQuery.Data,
      GetMetricQuery.Variables
  >(
    connector,
    GetMetricQuery.Companion.operationName,
    GetMetricQuery.Companion.dataDeserializer,
    GetMetricQuery.Companion.variablesSerializer,
  )


private class GetProjectQueryImpl(
  connector: ExampleConnector
):
  GetProjectQuery,
  ExampleConnectorGeneratedQueryImpl<
      GetProjectQuery.Data,
      GetProjectQuery.Variables
  >(
    connector,
    GetProjectQuery.Companion.operationName,
    GetProjectQuery.Companion.dataDeserializer,
    GetProjectQuery.Companion.variablesSerializer,
  )


private class GetSensorQueryImpl(
  connector: ExampleConnector
):
  GetSensorQuery,
  ExampleConnectorGeneratedQueryImpl<
      GetSensorQuery.Data,
      GetSensorQuery.Variables
  >(
    connector,
    GetSensorQuery.Companion.operationName,
    GetSensorQuery.Companion.dataDeserializer,
    GetSensorQuery.Companion.variablesSerializer,
  )


private class ListAlertsQueryImpl(
  connector: ExampleConnector
):
  ListAlertsQuery,
  ExampleConnectorGeneratedQueryImpl<
      ListAlertsQuery.Data,
      Unit
  >(
    connector,
    ListAlertsQuery.Companion.operationName,
    ListAlertsQuery.Companion.dataDeserializer,
    ListAlertsQuery.Companion.variablesSerializer,
  )


private class ListLocationsQueryImpl(
  connector: ExampleConnector
):
  ListLocationsQuery,
  ExampleConnectorGeneratedQueryImpl<
      ListLocationsQuery.Data,
      Unit
  >(
    connector,
    ListLocationsQuery.Companion.operationName,
    ListLocationsQuery.Companion.dataDeserializer,
    ListLocationsQuery.Companion.variablesSerializer,
  )


private class ListMetricsQueryImpl(
  connector: ExampleConnector
):
  ListMetricsQuery,
  ExampleConnectorGeneratedQueryImpl<
      ListMetricsQuery.Data,
      Unit
  >(
    connector,
    ListMetricsQuery.Companion.operationName,
    ListMetricsQuery.Companion.dataDeserializer,
    ListMetricsQuery.Companion.variablesSerializer,
  )


private class ListProjectsQueryImpl(
  connector: ExampleConnector
):
  ListProjectsQuery,
  ExampleConnectorGeneratedQueryImpl<
      ListProjectsQuery.Data,
      Unit
  >(
    connector,
    ListProjectsQuery.Companion.operationName,
    ListProjectsQuery.Companion.dataDeserializer,
    ListProjectsQuery.Companion.variablesSerializer,
  )


private class ListSensorsQueryImpl(
  connector: ExampleConnector
):
  ListSensorsQuery,
  ExampleConnectorGeneratedQueryImpl<
      ListSensorsQuery.Data,
      Unit
  >(
    connector,
    ListSensorsQuery.Companion.operationName,
    ListSensorsQuery.Companion.dataDeserializer,
    ListSensorsQuery.Companion.variablesSerializer,
  )


private class UpdateAlertMutationImpl(
  connector: ExampleConnector
):
  UpdateAlertMutation,
  ExampleConnectorGeneratedMutationImpl<
      UpdateAlertMutation.Data,
      UpdateAlertMutation.Variables
  >(
    connector,
    UpdateAlertMutation.Companion.operationName,
    UpdateAlertMutation.Companion.dataDeserializer,
    UpdateAlertMutation.Companion.variablesSerializer,
  )


private class UpdateLocationMutationImpl(
  connector: ExampleConnector
):
  UpdateLocationMutation,
  ExampleConnectorGeneratedMutationImpl<
      UpdateLocationMutation.Data,
      UpdateLocationMutation.Variables
  >(
    connector,
    UpdateLocationMutation.Companion.operationName,
    UpdateLocationMutation.Companion.dataDeserializer,
    UpdateLocationMutation.Companion.variablesSerializer,
  )


private class UpdateMetricMutationImpl(
  connector: ExampleConnector
):
  UpdateMetricMutation,
  ExampleConnectorGeneratedMutationImpl<
      UpdateMetricMutation.Data,
      UpdateMetricMutation.Variables
  >(
    connector,
    UpdateMetricMutation.Companion.operationName,
    UpdateMetricMutation.Companion.dataDeserializer,
    UpdateMetricMutation.Companion.variablesSerializer,
  )


private class UpdateProjectMutationImpl(
  connector: ExampleConnector
):
  UpdateProjectMutation,
  ExampleConnectorGeneratedMutationImpl<
      UpdateProjectMutation.Data,
      UpdateProjectMutation.Variables
  >(
    connector,
    UpdateProjectMutation.Companion.operationName,
    UpdateProjectMutation.Companion.dataDeserializer,
    UpdateProjectMutation.Companion.variablesSerializer,
  )


private class UpdateSensorMutationImpl(
  connector: ExampleConnector
):
  UpdateSensorMutation,
  ExampleConnectorGeneratedMutationImpl<
      UpdateSensorMutation.Data,
      UpdateSensorMutation.Variables
  >(
    connector,
    UpdateSensorMutation.Companion.operationName,
    UpdateSensorMutation.Companion.dataDeserializer,
    UpdateSensorMutation.Companion.variablesSerializer,
  )


