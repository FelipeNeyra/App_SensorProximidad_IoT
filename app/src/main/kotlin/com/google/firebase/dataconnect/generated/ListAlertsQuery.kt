
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


public interface ListAlertsQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      ExampleConnector,
      ListAlertsQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val alerts: List<AlertsItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class AlertsItem(
  
    val severity: String,
  
    val message: String,
  
    val createdAt: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.TimestampSerializer::class) com.google.firebase.Timestamp,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "ListAlerts"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun ListAlertsQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    ListAlertsQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun ListAlertsQuery.execute(

  
    fetchPolicy: com.google.firebase.dataconnect.QueryRef.FetchPolicy = com.google.firebase.dataconnect.QueryRef.FetchPolicy.PREFER_CACHE,
  

  ): com.google.firebase.dataconnect.QueryResult<
    ListAlertsQuery.Data,
    Unit
  > =
  ref(
    
  ).execute(fetchPolicy = fetchPolicy)


  public fun ListAlertsQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<ListAlertsQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

