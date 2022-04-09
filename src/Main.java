
public class Main {

    public static void StateArgs(String args[]) {
        System.out.println("Possible Arg are:");
        System.out.println("-S # -C N");
        for(String s : args)
            System.out.print(s + " ");
        System.out.println("");
    }

    public static void main(String args[]) {
        if (args.length >= 4) {
            if (args[0].equals("-S")) {
                int T = -1;
                try {
                    T = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    StateArgs(args);
                    return;
                }
                switch (T) {
                    case 1: {
                        if (args[2].equals("-C")) {
                            int C = -1;
                            try {
                                C = Integer.parseInt(args[3]);
                            } catch (Exception e) {
                                StateArgs(args);
                                return;
                            }
                            Cris.CPU.Setup(C, 0);
                        } else {
                            StateArgs(args);
                            return;
                        }
                        break;
                    }
                    case 2: {
                        int quantum = -1;
                        try {
                            quantum = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            StateArgs(args);
                            return;
                        }

                        if (args[3].equals("-C")) {
                            int C = -1;
                            try {
                                C = Integer.parseInt(args[4]);
                            } catch (Exception e) {
                                StateArgs(args);
                                return;
                            }                        System.out.println("ere");

                            company.CPU.Start(C, 1, quantum);
                        } else {
                            StateArgs(args);
                            return;
                        }
                        break;
                    }
                    case 3: {
                        if (args[2].equals("-C")) {
                            int C = -1;
                            try {
                                C = Integer.parseInt(args[3]);
                            } catch (Exception e) {
                                StateArgs(args);
                                return;
                            }
                            company.CPU.Start(C, 0, 0);
                        } else {
                            StateArgs(args);
                            return;
                        }
                        break;
                    }
                    default: {
                        StateArgs(args);
                        return;
                    }

                }

            } else {
                StateArgs(args);
            }
        } else {
            if(args.length == 2 && args[0].equals("-S") && args[1].equals("4")){
                Cris.CPU.Setup(1, 1);
            } else {
                StateArgs(args);

            }
        }
    }


}
