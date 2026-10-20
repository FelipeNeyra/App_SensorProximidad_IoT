
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


import kotlinx.coroutines.flow.filterNotNull as _flow_filterNotNull
import kotlinx.coroutines.flow.map as _flow_map


public interface GetSensorQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      ExampleConnector,
      GetSensorQuery.Data,
      GetSensorQuery.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val sensor: Sensor?,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Sensor(
  
    val type: String,
  
    val status: String,
  
    val lastPingAt: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.TimestampSerializer::class) com.google.firebase.Timestamp,
  
    val batteryLevel: Int?,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetSensor"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun GetSensorQuery.ref(
  
    id: java.util.UUID,

  
  
): com.google.firebase.dataconnect.QueryRef<
    GetSensorQuery.Data,
    GetSensorQuery.Variables
  > =
  ref(
    
      GetSensorQuery.Variables(
        id=id,
  
      )
    
  )

public suspend fun GetSensorQuery.execute(

  
    
      id: java.util.UUID,
  fetchPolicy: com.google.firebase.dataconnect.QueryRef.FetchPolicy = com.google.firebase.dataconnect.QueryRef.FetchPolicy.PREFER_CACHE,
  

  ): com.google.firebase.dataconnect.QueryResult<
    GetSensorQuery.Data,
    GetSensorQuery.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute(fetchPolicy = fetchPolicy)


  public fun GetSensorQuery.flow(
    
      id: java.util.UUID,

  
    
    ): kotlinx.coroutines.flow.Flow<GetSensorQuery.Data> =
    ref(
        
          id=id,
  
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

