
// Service to connect with local Ollama instance
// Ensure you run ollama with: OLLAMA_ORIGINS="*" ollama serve

export const getRepairDiagnosis = async (deviceType: string, problem: string): Promise<string> => {
  try {
    // URL default for local Ollama
    const OLLAMA_URL = 'http://localhost:11434/api/generate';

    // Construct the prompt
    const promptText = `
      Actúa como un técnico experto en reparaciones de hardware.
      Equipo: ${deviceType}
      Problema reportado: "${problem}"
      
      Por favor, provee:
      1. Diagnóstico técnico probable.
      2. Causas posibles.
      3. Repuestos sugeridos.
      
      Sé conciso, profesional y usa formato de lista. Responde en Español.
    `;

    const response = await fetch(OLLAMA_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        model: 'phi4-mini', // Microsoft Phi-4 mini model
        prompt: promptText,
        stream: false, // False to get the full response at once
        options: {
          temperature: 0.7 // Balanced creativity/precision
        }
      }),
    });

    if (!response.ok) {
      throw new Error(`Ollama Error: ${response.statusText}`);
    }

    const data = await response.json();
    let rawText = data.response;

    // Some models include <think> tags for reasoning. We remove them.
    // We remove them to show only the final result to the user.
    const cleanText = rawText.replace(/<think>[\s\S]*?<\/think>/g, '').trim();

    return cleanText || "No se generó diagnóstico.";

  } catch (error) {
    console.error("AI Service Error:", error);
    return "Error de conexión con Ollama local. \n\n1. Asegúrate de que Ollama esté corriendo (ollama serve). \n2. Verifica que tengas el modelo (ollama pull phi4-mini). \n3. Importante: Configura OLLAMA_ORIGINS='*' para permitir conexiones del navegador.";
  }
};
