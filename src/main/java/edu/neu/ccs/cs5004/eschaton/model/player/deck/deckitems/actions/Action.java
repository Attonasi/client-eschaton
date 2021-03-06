package edu.neu.ccs.cs5004.eschaton.model.player.deck.deckitems.actions;

import edu.neu.ccs.cs5004.eschaton.model.player.Player;

public abstract class Action implements ActionInterface{

  protected Integer[] cost = {0, 0, 0, 0, 0, 0};
  protected String name;

  public Action(Integer[] cost) { this.cost = cost; }

  public Action(){ }

  public String getName() {
    return name;
  }

  public Integer[] getCost(){
    return cost;
  }

  /**
   * Every Action has a cost. This will be a helper method called byt the playAction method to adjust the player's
   * bank totals.
   *
   * @param player Player that played the action and needs to spend money to do so.
   */
  public void payCost(Player player, Integer[] cost) {
    for(int amount : cost){
      player.getBankArray()[amount] -= cost[amount];
    }
  }
}
