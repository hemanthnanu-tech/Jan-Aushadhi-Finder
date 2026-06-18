package com.example.jan_aushadhifinder.service

object GenAIService {
    fun getMedicineInsights(brandName: String): String {
        val insights = mapOf(
            "Dolo 650" to "Generic Paracetamol is chemically identical to Dolo 650. Switching saves you 60% per strip with the same pain-relief efficacy.",
            "Telma 40" to "Generic Telmisartan 40mg offers the exact same blood pressure control as Telma. It is a WHO-GMP certified alternative.",
            "Pan 40" to "Generic Pantoprazole treats acidity identically to Pan 40. Generic costs ₹15 vs ₹60 for branded.",
            "Augmentin 625 Duo" to "Generic Amoxycillin + Clavulanic Acid is the exact clinical equivalent. Massive savings on this chronic antibiotic.",
            "Glycomet GP 2" to "Generic Metformin + Glimepiride provides identical blood sugar management. Switch safely to save ₹100+ monthly.",
            "Amlong 5" to "Generic Amlodipine is essential for BP control. Highly affordable generic salt.",
            "Atorva 10" to "Generic Atorvastatin reduces cholesterol identically to the brand. Save 70%.",
            "Rosuvas 10" to "Generic Rosuvastatin is a high-potency statin. Generic switch saves significantly.",
            "Ecosprin 75" to "Generic Aspirin is critical for heart health. Generic cost is negligible."
        )
        
        return insights[brandName] ?: "Generic equivalent contains the same active salt '$brandName'. WHO-GMP certified generics ensure identical quality, safety, and efficacy at significantly lower costs."
    }
}
