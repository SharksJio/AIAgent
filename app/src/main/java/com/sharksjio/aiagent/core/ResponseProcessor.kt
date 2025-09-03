package com.sharksjio.aiagent.core

/**
 * Processes and aggregates responses from multiple agents
 */
class ResponseProcessor {
    
    /**
     * Aggregates multiple responses into a single, comprehensive response
     */
    fun aggregateResponses(responses: List<AgentResponse>): AgentResponse {
        if (responses.isEmpty()) {
            return createEmptyResponse()
        }
        
        if (responses.size == 1) {
            return responses.first()
        }
        
        val bestResponse = responses.maxByOrNull { it.confidence } ?: responses.first()
        val averageConfidence = responses.map { it.confidence }.average()
        val totalProcessingTime = responses.sumOf { it.processingTime }
        
        val aggregatedContent = buildString {
            appendLine("Comprehensive AI Analysis:")
            appendLine("=" .repeat(40))
            
            responses.forEachIndexed { index, response ->
                appendLine("\n${index + 1}. ${response.metadata["agentName"] ?: "Agent"} (Confidence: ${String.format("%.1f", response.confidence * 100)}%)")
                appendLine(response.content)
                appendLine("-".repeat(40))
            }
            
            appendLine("\nSummary:")
            appendLine("✓ Best Response Confidence: ${String.format("%.1f", bestResponse.confidence * 100)}%")
            appendLine("✓ Average Confidence: ${String.format("%.1f", averageConfidence * 100)}%")
            appendLine("✓ Total Processing Time: ${totalProcessingTime}ms")
        }
        
        return AgentResponse(
            id = bestResponse.id,
            agentId = "aggregated",
            content = aggregatedContent,
            confidence = averageConfidence,
            topic = bestResponse.topic,
            processingTime = totalProcessingTime,
            metadata = mapOf(
                "aggregated" to true,
                "agentCount" to responses.size,
                "bestAgentId" to bestResponse.agentId
            )
        )
    }
    
    /**
     * Formats a single response for display
     */
    fun formatResponse(response: AgentResponse): String {
        return buildString {
            appendLine("AI Agent Response")
            appendLine("Agent: ${response.metadata["agentName"] ?: response.agentId}")
            appendLine("Topic: ${response.topic}")
            appendLine("Confidence: ${String.format("%.1f", response.confidence * 100)}%")
            appendLine("Processing Time: ${response.processingTime}ms")
            appendLine()
            appendLine("Response:")
            appendLine(response.content)
        }
    }
    
    /**
     * Validates response quality
     */
    fun validateResponse(response: AgentResponse): Boolean {
        return response.content.isNotBlank() && 
               response.confidence >= 0.0 && 
               response.confidence <= 1.0 &&
               !response.metadata.containsKey("error")
    }
    
    private fun createEmptyResponse(): AgentResponse {
        return AgentResponse(
            id = "empty",
            agentId = "system",
            content = "No responses were generated.",
            confidence = 0.0,
            topic = TopicCategory.GENERAL,
            processingTime = 0L,
            metadata = mapOf("error" to true)
        )
    }
}