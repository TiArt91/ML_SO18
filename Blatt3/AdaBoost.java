package Sheet3_1;

import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;


public class AdaBoost {





    public static void main(String[] args) throws IOException {

        Instances data = readData("car.arff");

        //System.out.println("num: " + test.numInstances());
        //System.out.println("weight: " + test.instance(0).weight());

        data.resampleWithWeights(new Random(100));




    }


    public static Instances readData(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(
                                        new FileReader(fileName));
        Instances data = new Instances(reader);
        reader.close();

        for(int i=0; i<data.numInstances(); i++){
            data.instance(i).setWeight((double)1/data.numInstances());
        }
        return data;
    }
















}
