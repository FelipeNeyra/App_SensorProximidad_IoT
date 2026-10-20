
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


public interface GetProjectQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      ExampleConnector,
      GetProjectQuery.Data,
      GetProjectQuery.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val project: Project?,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Project(
  
    val name: String,
  
    val startDate: com.google.firebase.dataconnect.LocalDate?,
  
    val endDate: com.google.firebase.dataconnect.LocalDate?,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetProject"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun GetProjectQuery.ref(
  
    id: java.util.UUID,

  
  
): com.google.firebase.dataconnect.QueryRef<
    GetProjectQuery.Data,
    GetProjectQuery.Variables
  > =
  ref(
    
      GetProjectQuery.Variables(
        id=id,
  
      )
    
  )

public suspend fun GetProjectQuery.execute(

  
    
      id: java.util.UUID,
  fetchPolicy: com.google.firebase.dataconnect.QueryRef.FetchPolicy = com.google.firebase.dataconnect.QueryRef.FetchPolicy.PREFER_CACHE,
  

  ): com.google.firebase.dataconnect.QueryResult<
    GetProjectQuery.Data,
    GetProjectQuery.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute(fetchPolicy = fetchPolicy)


  public fun GetProjectQuery.flow(
    
      id: java.util.UUID,

  
    
    ): kotlinx.coroutines.flow.Flow<GetProjectQuery.Data> =
    ref(
        
          id=id,
  
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

