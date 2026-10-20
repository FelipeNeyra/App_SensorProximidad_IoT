
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



public interface UpdateProjectMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      ExampleConnector,
      UpdateProjectMutation.Data,
      UpdateProjectMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val endDate: com.google.firebase.dataconnect.OptionalVariable<com.google.firebase.dataconnect.LocalDate?>,
  
  ) {
    
    
      
      @kotlin.DslMarker public annotation class BuilderDsl

      
      @BuilderDsl
      public interface Builder {
        public var id: java.util.UUID
        public var endDate: com.google.firebase.dataconnect.LocalDate?
        
      }

      public companion object {
        
        @Suppress("NAME_SHADOWING")
        public fun build(
          id: java.util.UUID,
          block_: Builder.() -> Unit
        ): Variables {
          var id= id
            var endDate: com.google.firebase.dataconnect.OptionalVariable<com.google.firebase.dataconnect.LocalDate?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            

          return object : Builder {
            override var id: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { id = value_ }
              
            override var endDate: com.google.firebase.dataconnect.LocalDate?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { endDate = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            
          }.apply(block_)
          .let {
            Variables(
              id=id,endDate=endDate,
            )
          }
        }
      }
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val project_update: ProjectKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateProject"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun UpdateProjectMutation.ref(
  
    id: java.util.UUID,

  
    block_: UpdateProjectMutation.Variables.Builder.() -> Unit = {}
  
): com.google.firebase.dataconnect.MutationRef<
    UpdateProjectMutation.Data,
    UpdateProjectMutation.Variables
  > =
  ref(
    
      UpdateProjectMutation.Variables.build(
        id=id,
  
    block_
      )
    
  )

public suspend fun UpdateProjectMutation.execute(

  
    
      id: java.util.UUID,

  
    block_: UpdateProjectMutation.Variables.Builder.() -> Unit = {}

  ): com.google.firebase.dataconnect.MutationResult<
    UpdateProjectMutation.Data,
    UpdateProjectMutation.Variables
  > =
  ref(
    
      id=id,
  
    block_
    
  ).execute()


