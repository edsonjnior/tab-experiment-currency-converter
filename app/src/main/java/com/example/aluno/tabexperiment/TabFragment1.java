package com.example.aluno.tabexperiment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.aluno.tabexperiment.beans.Currency;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment1 extends Fragment {

    private EditText eCurrencyFrom;
    private EditText eCurrencyTo;
    private String doubleFormatted;


    public TabFragment1() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        eCurrencyFrom = getView().findViewById(R.id.edit_text_from);
        eCurrencyTo = getView().findViewById(R.id.edit_text_to);

        Button btnConvert = getView().findViewById(R.id.btn_convert);
        doubleFormatted = "0.00";
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convert();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_fragment1, container, false);
    }


    public void convert() {
        String todayRate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Currency currency = new Currency("USD", "BRL", todayRate, eCurrencyFrom.getText().toString());

        post(currency);
    }

    private void post(final Currency currency) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://currencyconverter.kowabunga.net/converter.asmx/GetConversionAmount";
                Map<String, String> params = new HashMap<>();
                params.put("CurrencyFrom", currency.getCurrencyFrom());
                params.put("CurrencyTo", currency.getCurrencyTo());
                params.put("RateDate", currency.getRateDate());
                params.put("Amount", currency.getAmount());

                try {
                    Connection.Response response = Jsoup.connect(url).data(params).method(Connection.Method.POST).execute();
                    byte[] body = response.bodyAsBytes();
                    InputStream is = new ByteArrayInputStream(body);
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(is);

                    Element element = doc.getDocumentElement();
                    element.normalize();

                    NodeList nList = doc.getElementsByTagName("decimal");
                    if (nList.getLength() > 0) {
                        Node node = nList.item(0);
                        String nodeValue = node.getFirstChild().getNodeValue();
                        double amount = Double.parseDouble(nodeValue);
                        doubleFormatted = String.format("%.2f", amount);
                    }

                    if (isAdded()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                eCurrencyTo.setText(doubleFormatted);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
