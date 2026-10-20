
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


public interface ListProjectsQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      ExampleConnector,
      ListProjectsQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val projects: List<ProjectsItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class ProjectsItem(
  
    val name: String,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "ListProjects"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun ListProjectsQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    ListProjectsQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun ListProjectsQuery.execute(

  
    fetchPolicy: com.google.firebase.dataconnect.QueryRef.FetchPolicy = com.google.firebase.dataconnect.QueryRef.FetchPolicy.PREFER_CACHE,
  

  ): com.google.firebase.dataconnect.QueryResult<
    ListProjectsQuery.Data,
    Unit
  > =
  ref(
    
  ).execute(fetchPolicy = fetchPolicy)


  public fun ListProjectsQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<ListProjectsQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

