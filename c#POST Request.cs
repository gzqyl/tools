using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    class Program
    {
        private static readonly HttpClient client = new HttpClient();

        static void Main(string[] args)
        {

            var values = new Dictionary<string, string>
            {
               { "name", "hi..." }
            };

            var content = new FormUrlEncodedContent(values);

            string a = Program.PostXmlResponse("http://virtual_qq.com/p.php", content);

            Console.WriteLine(a);
            Console.ReadLine();
        }

        public static string PostXmlResponse(string url, FormUrlEncodedContent xmlString)

        {
          
            HttpClient httpClient = new HttpClient();

            HttpResponseMessage response = httpClient.PostAsync(url, xmlString).Result;

            if (response.IsSuccessStatusCode)
            {
                Task<string> t = response.Content.ReadAsStringAsync();
                return t.Result;
            }
            return string.Empty;
        }
    }
}
