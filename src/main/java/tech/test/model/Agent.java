package tech.test.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import tech.test.tools.RandomGenerator;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@ToString
public class Agent implements Serializable {

    public static final String BREED_C = "Breed_C";
    public static final String BREED_NC = "Breed_NC";
    private String agentBreed;
    private String policyID;
    private int age;
    private int socialGrade;
    private int paymentAtPurchase;
    private double attributeBrand;
    private double attributePrice;
    private double attributePromotions;
    private boolean autoRenew;
    private int inertiaForSwitch;
    private String switchState;
    private boolean recentlySwitched;

    public Agent() {
    }

    public Agent(String agentBreed,
                 String policyID,
                 int age, int socialGrade,
                 int paymentAtPurchase,
                 double attributeBrand,
                 double attributePrice,
                 double attributePromotions,
                 boolean autoRenew,
                 int inertiaForSwitch,
                 String switchState,
                 boolean recently_switched) {
        this.agentBreed = agentBreed;
        this.policyID = policyID;
        this.age = age;
        this.socialGrade = socialGrade;
        this.paymentAtPurchase = paymentAtPurchase;
        this.attributeBrand = attributeBrand;
        this.attributePrice = attributePrice;
        this.attributePromotions = attributePromotions;
        this.autoRenew = autoRenew;
        this.inertiaForSwitch = inertiaForSwitch;
        this.switchState = switchState;
        this.recentlySwitched = recently_switched;
    }

    public void setAgentBreed(String agentBreed) {
        this.agentBreed = agentBreed;
    }

    public void setPolicyID(String policyID) {
        this.policyID = policyID;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSocialGrade(int socialGrade) {
        this.socialGrade = socialGrade;
    }

    public void setPaymentAtPurchase(int paymentAtPurchase) {
        this.paymentAtPurchase = paymentAtPurchase;
    }

    public void setAttributeBrand(double attributeBrand) {
        this.attributeBrand = attributeBrand;
    }

    public void setAttributePrice(double attributePrice) {
        this.attributePrice = attributePrice;
    }

    public void setAttributePromotions(double attributePromotions) {
        this.attributePromotions = attributePromotions;
    }

    public void setAutoRenew(String autoRenew) {
        this.autoRenew = "1" .equals(autoRenew);
    }

    public void setInertiaForSwitch(int inertiaForSwitch) {
        this.inertiaForSwitch = inertiaForSwitch;
    }

    public void setSwitchState(String switch_state) {
        switchState = switch_state;
    }

    public void setRecentlySwitched(boolean recently_switched) {
        recentlySwitched = recently_switched;
    }

    public double calculateAffinity() {
        return this.getPaymentAtPurchase() / this.getAttributePrice() +
                (RandomGenerator.getRandom() * this.getAttributePromotions() * this.getInertiaForSwitch());
    }

    public Agent setAgentBreed(double brandFactor) {
        double affinity = calculateAffinity();

        if (this.getAgentBreed().equals(BREED_C) &&
                affinity < (this.getSocialGrade() * this.getAttributeBrand())) {
            this.agentBreed = BREED_NC;
            this.setSwitchState("lost");
            this.setRecentlySwitched(true);
            return this;
        }

        if (this.getAgentBreed().equals(BREED_NC) &&
                affinity < (this.getSocialGrade() * this.getAttributeBrand() * brandFactor)) {
            this.agentBreed = BREED_C;
            if (this.getSwitchState().equals("lost")) {
                this.setSwitchState("regained");
                this.setRecentlySwitched(true);
                return this;

            }
            this.setSwitchState("gained");
            this.setRecentlySwitched(true);
            return this;
        }

        this.setRecentlySwitched(false);
        return this;
    }

}
