package tech.test;

import tech.test.model.Agent;
import org.apache.spark.sql.*;
import tech.test.tools.RandomGenerator;

import static tech.test.model.Agent.BREED_C;
import static tech.test.model.Agent.BREED_NC;

public class Main {

    public static void main(String[] args) {

        Encoder<Agent> agentEncoder = Encoders.bean(Agent.class);
        Dataset<Agent> agentDataset = getAgentDataset(agentEncoder);
        RandomGenerator rg = new RandomGenerator();
        double brandFactor = rg.getBrandFactor(0.1, 2.9);

        for (int age = 1; age <= 15; age++) {
            agentDataset = agentDataset
                    .filter(agent -> !agent.isAutoRenew())
                    .map(agent -> agent.setAgentBreed(brandFactor), agentEncoder);

            generateModelOutputs(age, agentDataset);
        }
    }

    private static void generateModelOutputs(int age, Dataset<Agent> agentDataset) {
        generateAgentBreedOutputs(age, agentDataset, BREED_C);
        generateAgentBreedOutputs(age, agentDataset, BREED_NC);

        generateSwitchedStateOutputs(age, agentDataset, "lost");
        generateSwitchedStateOutputs(age, agentDataset, "regained");
        generateSwitchedStateOutputs(age, agentDataset, "gained");
    }

    private static void generateAgentBreedOutputs(int age, Dataset<Agent> agentDataset, String agentBreed) {
        agentDataset.filter(agent -> agent.getAgentBreed().equals(agentBreed))
                .drop("recentlySwitched")
                .drop("switchState")
                .write()
                .option("header", "true")
                .csv(String.format("src/main/outputs/%s_Agents_Year_%d", agentBreed, age));
    }

    private static void generateSwitchedStateOutputs(int age, Dataset<Agent> agentDataset, String switchedState) {
        agentDataset.filter(agent -> agent.getSwitchState().equals(switchedState) && agent.isRecentlySwitched())
                .drop("recentlySwitched")
                .drop("switchState")
                .write()
                .option("header", "true")
                .csv(String.format("src/main/outputs/Breed_C_%s_Agents_Year_%d", switchedState, age));
    }

    private static Dataset<Agent> getAgentDataset(Encoder<Agent> agentEncoder) {
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark SQL Example")
                .getOrCreate();

        Dataset<Row> csv = spark.read()
                .option("header", "true")
                .csv("src/main/resources/Simudyne_Backend_Test.csv");

        return csv
                .withColumn("switchState", functions.lit("neutral"))
                .withColumn("recentlySwitched", functions.lit(false))
                .withColumn("agentBreed", csv.col("Agent_Breed"))
                .withColumn("policyID", csv.col("Policy_ID"))
                .withColumn("age", csv.col("Age").cast("int"))
                .withColumn("socialGrade", csv.col("Social_Grade").cast("int"))
                .withColumn("paymentAtPurchase", csv.col("Payment_At_Purchase").cast("int"))
                .withColumn("attributeBrand", csv.col("Attribute_Brand").cast("double"))
                .withColumn("attributePrice", csv.col("Attribute_Price").cast("double"))
                .withColumn("attributePromotions", csv.col("Attribute_Promotions").cast("double"))
                .withColumn("autoRenew", csv.col("Auto_Renew"))
                .withColumn("inertiaForSwitch", csv.col("Inertia_for_Switch").cast("int"))
                .as(agentEncoder);
    }
}
