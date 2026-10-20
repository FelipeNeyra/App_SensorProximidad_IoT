
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


public interface GetAlertQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      ExampleConnector,
      GetAlertQuery.Data,
      GetAlertQuery.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val alert: Alert?,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Alert(
  
    val severity: String,
  
    val message: String,
  
    val createdAt: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.TimestampSerializer::class) com.google.firebase.Timestamp,
  
    val resolvedAt: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.TimestampSerializer::class) com.google.firebase.Timestamp?,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetAlert"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun GetAlertQuery.ref(
  
    id: java.util.UUID,

  
  
): com.google.firebase.dataconnect.QueryRef<
    GetAlertQuery.Data,
    GetAlertQuery.Variables
  > =
  ref(
    
      GetAlertQuery.Variables(
        id=id,
  
      )
    
  )

public suspend fun GetAlertQuery.execute(

  
    
      id: java.util.UUID,
  fetchPolicy: com.google.firebase.dataconnect.QueryRef.FetchPolicy = com.google.firebase.dataconnect.QueryRef.FetchPolicy.PREFER_CACHE,
  

  ): com.google.firebase.dataconnect.QueryResult<
    GetAlertQuery.Data,
    GetAlertQuery.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute(fetchPolicy = fetchPolicy)


  public fun GetAlertQuery.flow(
    
      id: java.util.UUID,

  
    
    ): kotlinx.coroutines.flow.Flow<GetAlertQuery.Data> =
    ref(
        
          id=id,
  
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

